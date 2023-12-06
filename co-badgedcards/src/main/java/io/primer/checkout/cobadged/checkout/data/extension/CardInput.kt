package io.primer.checkout.cobadged.checkout.data.extension

import io.primer.android.components.domain.core.models.card.PrimerCardData
import io.primer.checkout.cobadged.checkout.data.model.CardInput
import java.text.SimpleDateFormat
import java.util.Locale

fun CardInput.toPrimerCardData() =
    PrimerCardData(
        cardNumber,
        convertDate(expiryDateFormatted),
        cvv,
        cardHolderName,
        preferredCardNetwork
    )

private fun convertDate(input: String): String {
    return try {
        val inputFormat = SimpleDateFormat("MM/yy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MM/yyyy", Locale.getDefault())

        val date = inputFormat.parse(input)
        outputFormat.format(date)
    } catch (e: Exception) {
        input
    }
}
