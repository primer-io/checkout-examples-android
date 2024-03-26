package io.primer.checkout.dropin.configuration.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.primer.android.domain.PrimerCheckoutData
import io.primer.checkout.dropin.checkout.data.repository.PrimerDropInEvent
import io.primer.checkout.dropin.checkout.data.repository.PrimerDropInRepository
import io.primer.checkout.dropin.configuration.data.ClientSessionRepository
import io.primer.checkout.dropin.configuration.data.api.ClientSessionService
import io.primer.checkout.dropin.configuration.validation.ClientTokenValidator
import io.primer.checkout.dropin.result.ui.CheckoutData
import io.primer.checkout.dropin.result.ui.CheckoutResult
import io.primer.checkout.dropin.util.AsyncStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutDropInViewModel @Inject constructor(
    private val clientSessionRepository: ClientSessionRepository,
    private val clientTokenValidator: ClientTokenValidator,
    private val dropInRepository: PrimerDropInRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _clientToken = savedStateHandle.getStateFlow(CLIENT_TOKEN_SAVED_STATE_KEY, "")
    private val _serverUrl =
        savedStateHandle.getStateFlow(SERVER_URL_SAVED_STATE_KEY, ClientSessionService.BASE_URL)

    private val _clientTokenStatus = MutableStateFlow<AsyncStatus<String>>(AsyncStatus.Loading)
    private val _checkoutResult = MutableStateFlow<CheckoutResult?>(null)
    val checkoutResult = _checkoutResult.asStateFlow()

    val checkoutUiState: StateFlow<CheckoutUiState> =
        combine(
            _clientToken,
            _serverUrl,
            _clientTokenStatus
        ) { clientToken, serverUrl, status ->
            when (status) {
                AsyncStatus.Loading -> CheckoutUiState(
                    serverUrl = serverUrl,
                    isLoading = true
                )

                is AsyncStatus.Error -> CheckoutUiState(
                    ClientTokenState(clientToken, clientTokenValidator.validate(clientToken)),
                    serverUrl,
                    false,
                    status.throwable.message
                )

                is AsyncStatus.Success -> CheckoutUiState(
                    ClientTokenState(status.data, clientTokenValidator.validate(status.data)),
                    serverUrl,
                    false,
                    null
                ).also { state ->
                    updateClientToken(state.clientTokenState.clientToken)
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = CheckoutUiState()
        )

    fun updateClientToken(clientToken: String) {
        savedStateHandle[CLIENT_TOKEN_SAVED_STATE_KEY] = clientToken
        _clientTokenStatus.update { AsyncStatus.Success(clientToken) }
    }

    fun updateServerUrl(serverUrl: String) {
        savedStateHandle[SERVER_URL_SAVED_STATE_KEY] = serverUrl
    }

    fun requestNewClientToken() =
        viewModelScope.launch {
            _clientTokenStatus.update { AsyncStatus.Loading }
            clientSessionRepository.createClientSession(_serverUrl.value).onSuccess { clientToken ->
                _clientTokenStatus.update { AsyncStatus.Success(clientToken) }
            }
                .onFailure { throwable ->
                    _clientTokenStatus.update { AsyncStatus.Error(throwable) }
                }
        }

    fun startCheckout(clientToken: String) {
        subscribeToEvents()
        dropInRepository.apply {
            start(clientToken)
        }
    }

    fun resetCheckoutResult() {
        _checkoutResult.update {
            null
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
        _checkoutResult.update {
            CheckoutResult.Success(
                checkoutData.payment.let { payment ->
                    CheckoutData(
                        payment.orderId,
                        payment.id
                    )
                }
            )
        }
    }

    private fun handleCheckoutError(
        errorMessage: String,
        checkoutData: PrimerCheckoutData?
    ) {
        _checkoutResult.update {
            CheckoutResult.Failed(
                errorMessage,
                checkoutData?.payment?.let { payment ->
                    CheckoutData(
                        payment.orderId,
                        payment.id
                    )
                }
            )
        }
    }

    data class CheckoutUiState(
        val clientTokenState: ClientTokenState = ClientTokenState.EMPTY,
        val serverUrl: String = "",
        val isLoading: Boolean = false,
        val errorMessage: String? = null
    )

    data class ClientTokenState(val clientToken: String, val isValid: Boolean) {

        companion object {
            val EMPTY = ClientTokenState("", false)
        }
    }

    private companion object {

        const val CLIENT_TOKEN_SAVED_STATE_KEY = "CLIENT_TOKEN_SAVED_STATE_KEY"
        const val SERVER_URL_SAVED_STATE_KEY = "SERVER_URL_SAVED_STATE_KEY"
    }
}
