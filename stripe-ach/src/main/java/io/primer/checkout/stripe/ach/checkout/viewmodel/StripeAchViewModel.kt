package io.primer.checkout.stripe.ach.checkout.viewmodel

import androidx.activity.result.ActivityResultRegistry
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import io.primer.android.components.domain.core.models.PrimerHeadlessUniversalCheckoutPaymentMethod
import io.primer.android.components.manager.core.composable.PrimerValidationStatus
import io.primer.android.components.presentation.paymentMethods.nativeUi.stripe.ach.composable.AchUserDetailsCollectableData
import io.primer.android.components.presentation.paymentMethods.nativeUi.stripe.ach.composable.AchUserDetailsStep
import io.primer.android.domain.PrimerCheckoutData
import io.primer.android.domain.error.models.PrimerError
import io.primer.checkout.stripe.ach.checkout.data.repository.DefaultPrimerHeadlessRepository.Companion.STRIPE_ACH_PAYMENT_METHOD_TYPE
import io.primer.checkout.stripe.ach.checkout.data.repository.PrimerHeadlessEvent
import io.primer.checkout.stripe.ach.checkout.data.repository.PrimerHeadlessRepository
import io.primer.checkout.stripe.ach.ui.CheckoutData
import io.primer.checkout.stripe.ach.ui.CheckoutResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface StripeAchUiState {

    data object Initializing : StripeAchUiState

    data class UserDetailsRetrieved(
        val firstName: String,
        val lastName: String,
        val emailAddress: String
    ) : StripeAchUiState

    data object UserDetailsCollected : StripeAchUiState

    data object DisplayMandate : StripeAchUiState

    data class CheckoutCompleted(val checkoutResult: CheckoutResult) : StripeAchUiState

    data class CheckoutError(val checkoutResult: CheckoutResult) : StripeAchUiState
}

data class UserDetailsValidationUiState(
    val isFirstNameValid: Boolean,
    val isLastNameValid: Boolean,
    val isEmailAddressValid: Boolean
)

@HiltViewModel(assistedFactory = StripeAchViewModel.ViewModelFactory::class)
class StripeAchViewModel @AssistedInject constructor(
    @Assisted("start") private val startComponent: () -> Unit,
    @Assisted private val step: () -> Flow<AchUserDetailsStep>,
    @Assisted private val error: () -> Flow<PrimerError>,
    @Assisted private val validation: () -> Flow<PrimerValidationStatus<AchUserDetailsCollectableData>>,
    @Assisted private val updateCollectedData: (AchUserDetailsCollectableData) -> Unit,
    @Assisted("submit") private val submitComponent: () -> Unit,
    private val headlessRepository: PrimerHeadlessRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<StripeAchUiState> =
        MutableStateFlow(StripeAchUiState.Initializing)
    val uiState get() = _uiState.asStateFlow()

    private var onAcceptMandate: (suspend () -> Unit)? = null

    private var onDeclineMandate: (suspend () -> Unit)? = null

    private val _validationUiState: MutableStateFlow<UserDetailsValidationUiState> =
        MutableStateFlow(
            UserDetailsValidationUiState(
                isFirstNameValid = true,
                isLastNameValid = true,
                isEmailAddressValid = true
            )
        )
    val validationUiState get() = _validationUiState.asStateFlow()

    fun startCheckout(clientToken: String, activityResultRegistry: ActivityResultRegistry) {
        subscribeToEvents()
        headlessRepository.start(clientToken, activityResultRegistry)
    }

    fun onSetFirstName(value: String) {
        updateCollectedData(AchUserDetailsCollectableData.FirstName(value))
    }

    fun onSetLastName(value: String) {
        updateCollectedData(AchUserDetailsCollectableData.LastName(value))
    }

    fun onSetEmailAddress(value: String) {
        updateCollectedData(AchUserDetailsCollectableData.EmailAddress(value))
    }

    fun submitUserDetails() {
        submitComponent()
    }

    fun acceptMandate() = viewModelScope.launch {
        onAcceptMandate?.invoke()
    }

    fun declineMandate() = viewModelScope.launch {
        onDeclineMandate?.invoke()
    }

    private fun collectFlows() {
        validation()
            .filterNot { it is PrimerValidationStatus.Validating }
            .onEach {
                when (it) {
                    is PrimerValidationStatus.Error -> _validationUiState.update { state ->
                        when (it.collectableData) {
                            is AchUserDetailsCollectableData.FirstName -> state.copy(isFirstNameValid = false)
                            is AchUserDetailsCollectableData.LastName -> state.copy(isLastNameValid = false)
                            is AchUserDetailsCollectableData.EmailAddress -> state.copy(isEmailAddressValid = false)
                            else -> error("Unsupported")
                        }
                    }

                    is PrimerValidationStatus.Invalid -> _validationUiState.update { state ->
                        when (it.collectableData) {
                            is AchUserDetailsCollectableData.FirstName -> state.copy(isFirstNameValid = false)
                            is AchUserDetailsCollectableData.LastName -> state.copy(isLastNameValid = false)
                            is AchUserDetailsCollectableData.EmailAddress -> state.copy(isEmailAddressValid = false)
                            else -> error("Unsupported")
                        }
                    }

                    is PrimerValidationStatus.Valid -> _validationUiState.update { state ->
                        when (it.collectableData) {
                            is AchUserDetailsCollectableData.FirstName -> state.copy(isFirstNameValid = true)
                            is AchUserDetailsCollectableData.LastName -> state.copy(isLastNameValid = true)
                            is AchUserDetailsCollectableData.EmailAddress -> state.copy(isEmailAddressValid = true)
                            else -> error("Unsupported")
                        }
                    }

                    else -> error("Unsupported")
                }
            }.launchIn(viewModelScope)

        error().onEach {
            _uiState.update {
                StripeAchUiState.CheckoutError(
                    CheckoutResult.Failed(
                        STRIPE_ACH_NOT_AVAILABLE_ERR0R,
                        null
                    )
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun subscribeToEvents() = viewModelScope.launch {
        headlessRepository.primerHeadlessEvents.collectLatest { event ->
            when (event) {
                is PrimerHeadlessEvent.AvailablePaymentMethodsLoaded ->
                    handleAvailablePaymentMethods(event.paymentMethods)

                is PrimerHeadlessEvent.CheckoutCompleted ->
                    handleCheckoutCompleted(event.checkoutData)

                is PrimerHeadlessEvent.CheckoutFailed -> handleCheckoutError(
                    event.errorMessage,
                    event.checkoutData
                )

                is PrimerHeadlessEvent.DisplayMandateStripeAchMandate -> {
                    onAcceptMandate = event.onAcceptMandate
                    onDeclineMandate = event.onDeclineMandate
                    _uiState.update { StripeAchUiState.DisplayMandate }
                }
            }
        }
    }

    private suspend fun handleAvailablePaymentMethods(
        paymentMethods: List<PrimerHeadlessUniversalCheckoutPaymentMethod>
    ) {
        val isStripeAchAllowed = paymentMethods.find { paymentMethod ->
            paymentMethod.paymentMethodType == STRIPE_ACH_PAYMENT_METHOD_TYPE
        } != null

        if (isStripeAchAllowed) {
            collectFlows()

            step().onEach { step ->
                _uiState.update {
                    when (step) {
                        is AchUserDetailsStep.UserDetailsRetrieved -> {
                            StripeAchUiState.UserDetailsRetrieved(
                                firstName = step.firstName,
                                lastName = step.lastName,
                                emailAddress = step.emailAddress
                            )
                        }

                        AchUserDetailsStep.UserDetailsCollected -> StripeAchUiState.UserDetailsCollected
                    }
                }
            }.launchIn(viewModelScope)
            startComponent()
        } else {
            _uiState.update {
                StripeAchUiState.CheckoutError(
                    checkoutResult = CheckoutResult.Failed(
                        error = STRIPE_ACH_NOT_AVAILABLE_ERR0R,
                        checkoutData = null
                    )
                )
            }
        }
    }

    private suspend fun handleCheckoutCompleted(checkoutData: PrimerCheckoutData?) {
        _uiState.update {
            StripeAchUiState.CheckoutCompleted(
                checkoutResult = CheckoutResult.Success(
                    checkoutData = checkoutData?.payment?.let { payment ->
                        CheckoutData(
                            orderId = payment.orderId,
                            paymentId = payment.id
                        )
                    }
                )
            )
        }
    }

    private suspend fun handleCheckoutError(errorMessage: String, checkoutData: PrimerCheckoutData?) {
        _uiState.update {
            StripeAchUiState.CheckoutError(
                checkoutResult = CheckoutResult.Failed(
                    error = errorMessage,
                    checkoutData = checkoutData?.payment?.let { payment ->
                        CheckoutData(
                            orderId = payment.orderId,
                            paymentId = payment.id
                        )
                    }
                )
            )
        }
    }

    @AssistedFactory
    fun interface ViewModelFactory {
        fun create(
            @Assisted("start") startComponent: () -> Unit,
            step: () -> Flow<AchUserDetailsStep>,
            error: () -> Flow<PrimerError>,
            validation: () -> Flow<PrimerValidationStatus<AchUserDetailsCollectableData>>,
            updateCollectedData: (AchUserDetailsCollectableData) -> Unit,
            @Assisted("submit") submitComponent: () -> Unit
        ): StripeAchViewModel
    }

    private companion object {

        const val STRIPE_ACH_NOT_AVAILABLE_ERR0R =
            "Stripe ACH is not available for the current session."
    }
}
