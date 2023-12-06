package io.primer.checkout.cobadged.checkout.data.model

import android.graphics.drawable.Drawable
import io.primer.android.ui.CardNetwork

data class CardNetworkMetadata(
    val type: CardNetwork.Type,
    val displayName: String,
    val resource: Drawable?
)

sealed interface CardNetworksState {

    data class CardNetworksChanged(
        val availableCardNetworks: List<CardNetworkMetadata>,
        val preferredCardNetwork: CardNetworkMetadata
    ) : CardNetworksState

    data object CardNetworksLoading : CardNetworksState
}
