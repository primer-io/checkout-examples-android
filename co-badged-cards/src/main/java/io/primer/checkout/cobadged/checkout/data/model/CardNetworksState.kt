package io.primer.checkout.cobadged.checkout.data.model

import android.graphics.drawable.Drawable
import io.primer.android.ui.CardNetwork

data class CardNetworksMetadata(
    val networks: List<CardNetworkDisplay>,
    val preferred: CardNetworkDisplay?
)

data class CardNetworkDisplay(
    val type: CardNetwork.Type,
    val displayName: String,
    val resource: Drawable?
)

sealed interface CardNetworksState {

    data class CardNetworksChanged(
        val selectableCardNetworks: CardNetworksMetadata?,
        val detectCardNetworks: CardNetworksMetadata
    ) : CardNetworksState

    data object CardNetworksLoading : CardNetworksState
}
