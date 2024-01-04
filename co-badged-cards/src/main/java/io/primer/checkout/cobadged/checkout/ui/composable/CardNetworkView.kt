package io.primer.checkout.cobadged.checkout.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.dimensionResource
import androidx.core.graphics.drawable.toBitmap
import io.primer.checkout.cobadged.R
import io.primer.checkout.cobadged.checkout.data.model.CardNetworkMetadata

@Composable
fun CardNetworkView(
    cardNetworkMetadata: CardNetworkMetadata,
    modifier: Modifier = Modifier
) {
    Row {
        cardNetworkMetadata.resource?.toBitmap()?.asImageBitmap()?.let { bitmap ->
            Image(
                bitmap = bitmap,
                contentDescription = cardNetworkMetadata.displayName,
                modifier = modifier
                    .height(dimensionResource(id = R.dimen.card_network_image_size))
                    .padding(
                        horizontal = dimensionResource(id = R.dimen.card_network_image_spacing)
                    )
            )
        }
    }
    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.spacing_default)))
}
