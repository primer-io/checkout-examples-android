package io.primer.checkout.cobadged.checkout.data.extension

import io.primer.android.components.domain.core.models.card.PrimerCardData
import io.primer.checkout.cobadged.checkout.data.model.CardInput

fun CardInput.toPrimerCardData() = PrimerCardData(
    cardNumber,
    expiryDateFormatted,
    cvv,
    cardHolderName,
    preferredCardNetwork
)
