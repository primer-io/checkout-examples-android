package io.primer.checkout.cobadged.configuration.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ClientSessionRequest(val amount: Int, val currencyCode: String)
