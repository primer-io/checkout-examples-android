package io.primer.checkout.cobadged.checkout.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.primer.android.components.domain.core.models.PrimerHeadlessUniversalCheckoutPaymentMethod
import io.primer.android.domain.PrimerCheckoutData
import io.primer.checkout.cobadged.checkout.data.repository.CardInputRepository
import io.primer.checkout.cobadged.checkout.data.repository.PrimerHeadlessEvent
import io.primer.checkout.cobadged.checkout.data.repository.PrimerHeadlessRepository
import io.primer.checkout.cobadged.checkout.data.model.CardInput
import io.primer.checkout.cobadged.checkout.data.model.CardNetworkMetadata
import io.primer.checkout.cobadged.checkout.data.model.CardNetworksState
import io.primer.checkout.cobadged.checkout.data.model.CardValidation
import io.primer.checkout.cobadged.checkout.data.repository.DefaultPrimerHeadlessRepository.Companion.CARD_PAYMENT_METHOD_TYPE
import io.primer.checkout.cobadged.result.ui.CheckoutData
import io.primer.checkout.cobadged.result.ui.CheckoutResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class CardFormUiState {

    data object Initializing : CardFormUiState()

    data class ShowCardForm(
        val showCardholderName: Boolean,
        val supportedCardNetworks: List<CardNetworkMetadata>
    ) : CardFormUiState()

    data object Submitted : CardFormUiState()

    data class CheckoutCompleted(val checkoutResult: CheckoutResult) : CardFormUiState()

    data class CheckoutError(val checkoutResult: CheckoutResult) : CardFormUiState()
}

@HiltViewModel
class CardInputViewModel @Inject constructor(
    private val cardInputRepository: CardInputRepository,
    private val headlessRepository: PrimerHeadlessRepository
) : ViewModel() {

    private val _cardFormUiState: MutableStateFlow<CardFormUiState> =
        MutableStateFlow(CardFormUiState.Initializing)
    val cardFormUiState = _cardFormUiState.asStateFlow()

    private val _cardInput: MutableStateFlow<CardInput> = MutableStateFlow(CardInput.EMPTY)
    val cardInput: StateFlow<CardInput> = _cardInput.asStateFlow()

    val cardValidationEvents: Flow<CardValidation> = cardInputRepository.validationEvents
    val cardNetworksStateEvents: Flow<CardNetworksState> =
        cardInputRepository.cardNetworksState

    fun onCardInputChanged(cardInput: CardInput) = _cardInput.update {
        cardInput
    }.also { cardInputRepository.updateCardData(cardInput) }

    fun submitData() {
        _cardFormUiState.update { CardFormUiState.Submitted }
        cardInputRepository.submit()
    }

    fun startCheckout(clientToken: String) {
        subscribeToEvents()
        headlessRepository.apply {
            start(clientToken)
        }
    }

    private fun subscribeToEvents() = viewModelScope.launch {
        headlessRepository.apply {
            primerHeadlessEvents.collectLatest { event ->
                when (event) {
                    is PrimerHeadlessEvent.AvailablePaymentMethodsLoaded ->
                        handleAvailablePaymentMethods(event.paymentMethods)

                    is PrimerHeadlessEvent.CheckoutCompleted ->
                        handleCheckoutCompleted(event.checkoutData)

                    is PrimerHeadlessEvent.CheckoutFailed -> handleCheckoutError(
                        event.errorMessage,
                        event.checkoutData
                    )
                }
            }
        }
    }

    private fun handleAvailablePaymentMethods(
        paymentMethods: List<PrimerHeadlessUniversalCheckoutPaymentMethod>
    ) {
        val isCardPaymentAllowed = paymentMethods.find { paymentMethod ->
            paymentMethod.paymentMethodType == CARD_PAYMENT_METHOD_TYPE
        } != null

        if (isCardPaymentAllowed) {
            _cardFormUiState.update {
                CardFormUiState.ShowCardForm(
                    cardInputRepository.isCardholderNameEnabled(),
                    cardInputRepository.getSupportedCardNetworks()
                )
            }
        } else {
            _cardFormUiState.update {
                CardFormUiState.CheckoutError(
                    CheckoutResult.Failed(
                        CARD_PAYMENT_NOT_AVAILABLE_ERR0R,
                        null
                    )
                )
            }
        }
    }

    private fun handleCheckoutCompleted(checkoutData: PrimerCheckoutData) {
        _cardFormUiState.update {
            CardFormUiState.CheckoutError(
                CheckoutResult.Success(
                    checkoutData.payment.let { payment ->
                        CheckoutData(
                            payment.orderId,
                            payment.id
                        )
                    }
                )
            )
        }
    }

    private fun handleCheckoutError(errorMessage: String, checkoutData: PrimerCheckoutData?) {
        _cardFormUiState.update {
            CardFormUiState.CheckoutError(
                CheckoutResult.Failed(
                    errorMessage,
                    checkoutData?.payment?.let { payment ->
                        CheckoutData(
                            payment.orderId,
                            payment.id
                        )
                    }
                )
            )
        }
    }

    private companion object {

        const val CARD_PAYMENT_NOT_AVAILABLE_ERR0R =
            "Payment card is not available for current session."
    }
}
