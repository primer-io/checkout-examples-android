package io.primer.checkout.cobadged.checkout.ui.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.core.graphics.drawable.toBitmap
import io.primer.checkout.cobadged.R
import io.primer.checkout.cobadged.checkout.data.model.CardNetworkDisplay

@Composable
fun CardNetworkPreviewView(
    cardNetworkDisplay: CardNetworkDisplay,
    modifier: Modifier = Modifier
) {
    Row {
        cardNetworkDisplay.resource?.toBitmap()?.asImageBitmap()?.let { bitmap ->
            Image(
                bitmap = bitmap,
                contentDescription = cardNetworkDisplay.displayName,
                contentScale = ContentScale.FillHeight,
                modifier = modifier
                    .height(dimensionResource(id = R.dimen.card_network_image_size))
                    .padding(
                        horizontal = dimensionResource(id = R.dimen.card_network_image_spacing)
                    )
                    .border(
                        BorderStroke(
                            width = dimensionResource(id = R.dimen.card_network_image_border_size),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        RoundedCornerShape(
                            dimensionResource(id = R.dimen.card_network_image_border_radius)
                        )
                    )
                    .alpha(
                        if (cardNetworkDisplay.allowed) {
                            1.0f
                        } else {
                            0.4f
                        }
                    )
            )
        }
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.spacing_default)))
    }
}
