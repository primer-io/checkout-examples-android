package io.primer.checkout.cobadged.configuration.data

import io.primer.checkout.cobadged.configuration.data.api.ClientSessionService
import io.primer.checkout.cobadged.configuration.data.model.ClientSessionRequest
import javax.inject.Inject

fun interface ClientSessionRepository {

    suspend fun createClientSession(url: String): Result<String>
}

class DefaultClientSessionRepository @Inject constructor(
    private val clientSessionService: ClientSessionService
) : ClientSessionRepository {

    override suspend fun createClientSession(url: String) = runCatching {
        clientSessionService.createClientSession(
            "$url/$CLIENT_SESSION_PATH",
            ClientSessionRequest(1000, "EUR")
        ).clientToken
    }

    private companion object {

        const val CLIENT_SESSION_PATH = "client-session"
    }
}
