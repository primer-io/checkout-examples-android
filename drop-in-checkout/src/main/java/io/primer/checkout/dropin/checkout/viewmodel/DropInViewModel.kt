package io.primer.checkout.dropin.checkout.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.primer.android.domain.PrimerCheckoutData
import io.primer.checkout.dropin.checkout.data.repository.PrimerDropInEvent
import io.primer.checkout.dropin.checkout.data.repository.PrimerDropInRepository
import io.primer.checkout.dropin.result.ui.CheckoutData
import io.primer.checkout.dropin.result.ui.CheckoutResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class DropInUiState {

    data object Initializing : DropInUiState()

    data class CheckoutCompleted(val checkoutResult: CheckoutResult) : DropInUiState()

    data class CheckoutError(val checkoutResult: CheckoutResult) : DropInUiState()
}

@HiltViewModel
class DropInViewModel @Inject constructor(
    private val dropInRepository: PrimerDropInRepository
) : ViewModel() {

    private val _dropInUiState: MutableStateFlow<DropInUiState> =
        MutableStateFlow(DropInUiState.Initializing)
    val dropInUiState = _dropInUiState.asStateFlow()

    fun startCheckout(clientToken: String) {
        subscribeToEvents()
        dropInRepository.apply {
            start(clientToken)
        }
    }

    private fun subscribeToEvents() = viewModelScope.launch {
        dropInRepository.apply {
            primerDropInEvents.collectLatest { event ->
                when (event) {
                    is PrimerDropInEvent.CheckoutCompleted ->
                        handleCheckoutCompleted(event.checkoutData)

                    is PrimerDropInEvent.CheckoutFailed -> handleCheckoutError(
                        event.errorMessage,
                        event.checkoutData
                    )
                }
            }
        }
    }

    private fun handleCheckoutCompleted(checkoutData: PrimerCheckoutData) {
        _dropInUiState.update {
            DropInUiState.CheckoutError(
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
        _dropInUiState.update {
            DropInUiState.CheckoutError(
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
}
