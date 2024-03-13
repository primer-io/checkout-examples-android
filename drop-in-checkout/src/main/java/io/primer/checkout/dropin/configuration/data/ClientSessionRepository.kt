package io.primer.checkout.dropin.configuration.data

import android.net.Uri
import io.primer.checkout.dropin.configuration.data.api.ClientSessionService
import javax.inject.Inject

fun interface ClientSessionRepository {

    suspend fun createClientSession(url: String): Result<String>
}

class DefaultClientSessionRepository @Inject constructor(
    private val clientSessionService: ClientSessionService
) : ClientSessionRepository {

    override suspend fun createClientSession(url: String) = runCatching {
        clientSessionService.createClientSession(
            Uri.parse(url).buildUpon().appendPath(CLIENT_SESSION_PATH).build().toString()
        ).clientToken
    }

    private companion object {

        const val CLIENT_SESSION_PATH = "client-session"
    }
}
