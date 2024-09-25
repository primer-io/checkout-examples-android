package io.primer.checkout.stripe.ach.configuration.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ClientSessionRequest(val amount: Int, val currencyCode: String)
