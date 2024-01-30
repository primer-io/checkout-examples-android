package io.primer.checkout.cobadged.configuration.data

import android.net.Uri
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
            Uri.parse(url).buildUpon().appendPath(CLIENT_SESSION_PATH).build().toString(),
            ClientSessionRequest(1000, "EUR")
        ).clientToken
    }

    private companion object {

        const val CLIENT_SESSION_PATH = "client-session"
    }
}
