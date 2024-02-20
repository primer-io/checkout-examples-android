package io.primer.checkout.cobadged.checkout.data.model

import android.graphics.drawable.Drawable
import io.primer.android.ui.CardNetwork

data class CardNetworkDisplay(
    val type: CardNetwork.Type,
    val displayName: String,
    val allowed: Boolean,
    val resource: Drawable?
)

sealed interface CardNetworksState {

    data class CardNetworksChanged(
        val networks: List<CardNetworkDisplay>,
        val shouldDisplaySelection: Boolean,
        val preferredSelectableNetwork: CardNetwork.Type?
    ) : CardNetworksState

    data object CardNetworksLoading : CardNetworksState
}
