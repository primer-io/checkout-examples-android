package io.primer.checkout.cobadged.checkout.ui.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.core.graphics.drawable.toBitmap
import io.primer.android.ui.CardNetwork
import io.primer.checkout.cobadged.R
import io.primer.checkout.cobadged.checkout.data.model.CardNetworkDisplay

@Composable
fun CardNetworkSelectionView(
    cardNetworks: List<CardNetworkDisplay>,
    preferredNetwork: CardNetwork.Type?,
    onCardNetworkSelected: (CardNetwork.Type) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCardNetwork by remember { mutableStateOf(preferredNetwork) }

    Row {
        cardNetworks.forEach { cardNetworkDisplay ->
            cardNetworkDisplay.resource?.toBitmap()?.asImageBitmap()?.let { bitmap ->
                Image(
                    bitmap = bitmap,
                    contentDescription = cardNetworkDisplay.displayName,
                    contentScale = ContentScale.FillHeight,
                    modifier = modifier
                        .clickable {
                            selectedCardNetwork = cardNetworkDisplay.type
                            onCardNetworkSelected(cardNetworkDisplay.type)
                        }
                        .height(dimensionResource(id = R.dimen.card_network_image_size))
                        .padding(
                            horizontal = dimensionResource(id = R.dimen.card_network_image_spacing)
                        )
                        .border(
                            BorderStroke(
                                width = dimensionResource(
                                    id = R.dimen.card_network_image_border_size
                                ),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            RoundedCornerShape(
                                dimensionResource(id = R.dimen.card_network_image_border_radius)
                            )
                        )
                        .alpha(
                            if (cardNetworkDisplay.type == selectedCardNetwork
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
