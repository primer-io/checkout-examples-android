package io.primer.checkout.stripe.ach.configuration.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ClientSessionResponse(val clientToken: String)
