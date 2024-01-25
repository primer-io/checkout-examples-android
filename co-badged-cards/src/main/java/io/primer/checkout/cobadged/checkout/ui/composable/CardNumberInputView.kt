package io.primer.checkout.cobadged.checkout.ui.composable

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import io.primer.android.ui.CardNetwork
import io.primer.checkout.cobadged.R
import io.primer.checkout.cobadged.checkout.data.model.CardNetworksState
import io.primer.checkout.cobadged.checkout.ui.formatting.CardFormat
import io.primer.checkout.cobadged.checkout.ui.formatting.MaskVisualTransformation

@Composable
fun CardNumberInputView(
    value: String,
    onValueChanged: (String) -> Unit,
    error: String?,
    onCardNetworkSelected: (CardNetwork.Type) -> Unit,
    cardNetworksState: CardNetworksState?,
    modifier: Modifier = Modifier
) {
    val cardFormat = when (cardNetworksState) {
        is CardNetworksState.CardNetworksChanged ->
            CardFormat.valueOf(
                cardNetworksState.networks.firstOrNull()?.type?.name
                    ?: CardFormat.OTHER.name
            )

        else -> CardFormat.OTHER
    }

    OutlinedTextField(
        value = value,
        onValueChange = {
            onValueChanged(it.take(cardFormat.cardNumberMaxLength()))
        },
        modifier = modifier,
        isError = value.isNotBlank() && error.isNullOrBlank().not(),
        label = { Text(text = stringResource(R.string.card_number)) },
        supportingText = { if (value.isNotBlank()) error?.let { Text(text = it) } },
        visualTransformation = MaskVisualTransformation(
            mask = cardFormat.mask
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        trailingIcon = {
            cardNetworksState?.let { state ->
                when (state) {
                    is CardNetworksState.CardNetworksChanged -> {
                        when (state.shouldDisplaySelection) {
                            true -> CardNetworkSelectionView(
                                state.networks,
                                state.preferredSelectableNetwork,
                                onCardNetworkSelected
                            )

                            false -> state.networks.firstOrNull()
                                ?.let { preferredNetwork ->
                                    CardNetworkPreviewView(preferredNetwork)
                                }
                        }
                    }

                    is CardNetworksState.CardNetworksLoading ->
                        CircularProgressIndicator(
                            modifier = Modifier
                                .height(dimensionResource(id = R.dimen.card_networks_loader_size))
                                .width(dimensionResource(id = R.dimen.card_networks_loader_size))
                        )
                }
            }
        },
        singleLine = true
    )
}
