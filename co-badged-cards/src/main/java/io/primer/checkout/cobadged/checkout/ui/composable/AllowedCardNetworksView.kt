package io.primer.checkout.cobadged.checkout.ui.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.core.graphics.drawable.toBitmap
import io.primer.checkout.cobadged.R
import io.primer.checkout.cobadged.checkout.data.model.CardNetworkDisplay

@Composable
fun AllowedCardNetworksView(
    allowedCardNetworks: List<CardNetworkDisplay>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .horizontalScroll(rememberScrollState())
            .padding(
                top = dimensionResource(id = R.dimen.spacing_half),
                bottom = dimensionResource(id = R.dimen.spacing_default)
            )
    ) {
        allowedCardNetworks.forEach { cardNetworkMetadata ->
            cardNetworkMetadata.resource?.toBitmap()?.asImageBitmap()?.let { bitmap ->
                Image(
                    bitmap = bitmap,
                    contentDescription = cardNetworkMetadata.displayName,
                    contentScale = ContentScale.FillHeight,
                    modifier = modifier
                        .padding(horizontal = dimensionResource(id = R.dimen.spacing_half))
                        .height(dimensionResource(id = R.dimen.card_network_image_size))
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
                )
            }
        }
    }
}
