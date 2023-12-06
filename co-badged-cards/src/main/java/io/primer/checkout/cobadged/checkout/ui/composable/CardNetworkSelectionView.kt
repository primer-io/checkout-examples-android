package io.primer.checkout.cobadged.checkout.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import io.primer.android.ui.CardNetwork
import io.primer.checkout.cobadged.R
import io.primer.checkout.cobadged.checkout.data.model.CardNetworkMetadata

@Composable
fun CardNetworkSelectionView(
    cardNetworks: List<CardNetworkMetadata>,
    selectedCardNetwork: CardNetwork.Type?,
    onCardNetworkSelected: (CardNetwork.Type) -> Unit,
    modifier: Modifier = Modifier
) {
    Row {
        cardNetworks.forEach { cardNetworkMetadata ->
            cardNetworkMetadata.resource?.toBitmap()?.asImageBitmap()?.let { bitmap ->
                Image(
                    bitmap = bitmap,
                    contentDescription = cardNetworkMetadata.displayName,
                    modifier = modifier
                        .clickable {
                            onCardNetworkSelected(cardNetworkMetadata.type)
                        }
                        .height(dimensionResource(id = R.dimen.card_network_image_size))
                        .alpha(
                            if (cardNetworkMetadata.type == selectedCardNetwork ||
                                selectedCardNetwork == null
                            ) {
                                1.0f
                            } else {
                                0.4f
                            }
                        )
                )
            }
        }
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.spacing_default)))
    }
}
