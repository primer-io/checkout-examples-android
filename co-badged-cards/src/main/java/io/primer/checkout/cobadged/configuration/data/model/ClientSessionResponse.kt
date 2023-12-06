package io.primer.checkout.cobadged.configuration.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ClientSessionResponse(val clientToken: String)
