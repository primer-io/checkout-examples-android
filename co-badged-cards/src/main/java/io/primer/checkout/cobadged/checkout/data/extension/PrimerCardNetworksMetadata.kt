package io.primer.checkout.cobadged.checkout.data.extension

import android.content.Context
import io.primer.android.components.domain.core.models.card.PrimerCardNetwork
import io.primer.android.components.ui.assets.PrimerHeadlessUniversalCheckoutAssetsManager
import io.primer.checkout.cobadged.checkout.data.model.CardNetworkDisplay

fun PrimerCardNetwork.toCardNetworkDisplay(context: Context) = CardNetworkDisplay(
    network,
    displayName,
    allowed,
    PrimerHeadlessUniversalCheckoutAssetsManager
        .getCardNetworkAsset(
            context,
            network
        ).cardImage
)
