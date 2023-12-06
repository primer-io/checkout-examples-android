package io.primer.checkout.cobadged.checkout.ui.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.res.dimensionResource
import io.primer.checkout.cobadged.R
import io.primer.checkout.cobadged.checkout.data.model.CardInput
import io.primer.checkout.cobadged.checkout.data.model.CardNetworksState
import io.primer.checkout.cobadged.checkout.data.model.ValidationErrors

private fun Modifier.cardInputModifier() = composed {
    this
        .fillMaxWidth()
        .padding(dimensionResource(id = R.dimen.vertical_margin_half))
}

@Composable
fun CardForm(
    input: CardInput,
    showCardholderName: Boolean,
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
