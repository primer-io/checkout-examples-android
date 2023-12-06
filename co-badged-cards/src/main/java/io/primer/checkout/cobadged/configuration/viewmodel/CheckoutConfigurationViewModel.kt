package io.primer.checkout.cobadged.configuration.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.primer.checkout.cobadged.configuration.data.ClientSessionRepository
import io.primer.checkout.cobadged.configuration.data.api.ClientSessionService
import io.primer.checkout.cobadged.configuration.validation.ClientTokenValidator
import io.primer.checkout.cobadged.util.AsyncStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutConfigurationViewModel @Inject constructor(
    private val clientSessionRepository: ClientSessionRepository,
    private val clientTokenValidator: ClientTokenValidator,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _clientToken = savedStateHandle.getStateFlow(CLIENT_TOKEN_SAVED_STATE_KEY, "")
    private val _serverUrl =
        savedStateHandle.getStateFlow(SERVER_URL_SAVED_STATE_KEY, ClientSessionService.BASE_URL)
    private val _clientTokenStatus = MutableStateFlow<AsyncStatus<String>>(AsyncStatus.Loading)

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
