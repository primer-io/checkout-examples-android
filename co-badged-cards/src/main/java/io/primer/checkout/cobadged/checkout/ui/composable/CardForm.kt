package io.primer.checkout.cobadged.checkout.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.dimensionResource
import androidx.core.graphics.drawable.toBitmap
import io.primer.checkout.cobadged.R
import io.primer.checkout.cobadged.checkout.data.model.CardInput
import io.primer.checkout.cobadged.checkout.data.model.CardNetworkMetadata
import io.primer.checkout.cobadged.checkout.data.model.CardNetworksState
import io.primer.checkout.cobadged.checkout.data.model.ValidationErrors

private fun Modifier.cardInputModifier() = composed {
    this
        .fillMaxWidth()
        .padding(horizontal = dimensionResource(id = R.dimen.spacing_half))
}

@Composable
fun CardForm(
    input: CardInput,
    showCardholderName: Boolean,
    supportedCardNetworks: List<CardNetworkMetadata>,
    validationErrors: ValidationErrors?,
    cardNetworksState: CardNetworksState?,
    onCardInputChanged: (CardInput) -> Unit,
    modifier: Modifier = Modifier
) {
    CardNumberInputView(
        input.cardNumber,
        onValueChanged = { cardNumber ->
            onCardInputChanged(input.copy(cardNumber = cardNumber))
        },
        input.preferredCardNetwork,
        validationErrors?.cardNumber,
        onCardNetworkSelected = { cardNetwork ->
            onCardInputChanged(input.copy(preferredCardNetwork = cardNetwork))
        },
        cardNetworksState,
        modifier = modifier.cardInputModifier()
    )
    Row(modifier = modifier.horizontalScroll(rememberScrollState())) {
        supportedCardNetworks.forEach { cardNetworkMetadata ->
            cardNetworkMetadata.resource?.toBitmap()?.asImageBitmap()?.let { bitmap ->
                Image(
                    bitmap = bitmap,
                    contentDescription = cardNetworkMetadata.displayName,
                    modifier = modifier
                        .padding(horizontal = dimensionResource(id = R.dimen.spacing_half))
                        .height(dimensionResource(id = R.dimen.card_network_image_size))
                )
            }
        }
    }
    Row {
        CardExpiryDateInput(
            input.expiryDate,
            onValueChanged = { expiryDate, expiryDateFormatted ->
                onCardInputChanged(
                    input.copy(
                        expiryDate = expiryDate,
                        expiryDateFormatted = expiryDateFormatted
                    )
                )
            },
            validationErrors?.cardExpiryDate,
            modifier
                .cardInputModifier()
                .weight(1f)
        )
        CardCvvInput(
            input.cvv,
            onValueChanged = { cvv ->
                onCardInputChanged(input.copy(cvv = cvv))
            },
            validationErrors?.cardCvv,
            modifier = modifier
                .cardInputModifier()
                .weight(1f)
        )
    }
    if (showCardholderName) {
        CardHolderNameInput(
            input.cardHolderName.orEmpty(),
            onValueChanged = { cardHolderName ->
                onCardInputChanged(input.copy(cardHolderName = cardHolderName))
            },
            validationErrors?.cardholderName,
            modifier = modifier.cardInputModifier()
        )
    }
}
