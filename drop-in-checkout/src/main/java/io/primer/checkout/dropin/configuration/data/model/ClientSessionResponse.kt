package io.primer.checkout.dropin.configuration.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ClientSessionResponse(val clientToken: String)
