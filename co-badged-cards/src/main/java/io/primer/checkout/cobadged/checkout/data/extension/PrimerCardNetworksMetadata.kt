package io.primer.checkout.cobadged.checkout.data.extension

import android.content.Context
import io.primer.android.components.domain.core.models.card.PrimerCardNetwork
import io.primer.android.components.domain.core.models.card.PrimerCardNetworksMetadata
import io.primer.android.components.ui.assets.PrimerHeadlessUniversalCheckoutAssetsManager
import io.primer.checkout.cobadged.checkout.data.model.CardNetworkDisplay
import io.primer.checkout.cobadged.checkout.data.model.CardNetworksMetadata

fun PrimerCardNetworksMetadata.toCardNetworksMetadata(context: Context) = CardNetworksMetadata(
    items.map { item -> item.toCardNetworkDisplay(context) },
    preferred?.toCardNetworkDisplay(context)
)

fun PrimerCardNetwork.toCardNetworkDisplay(context: Context) = CardNetworkDisplay(
    network,
    displayName,
    PrimerHeadlessUniversalCheckoutAssetsManager
        .getCardNetworkAssets(
            context,
            network
        ).cardImage
)
