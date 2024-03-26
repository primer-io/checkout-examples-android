package io.primer.checkout.dropin.configuration.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ClientSessionRequest(val amount: Int, val currencyCode: String)
