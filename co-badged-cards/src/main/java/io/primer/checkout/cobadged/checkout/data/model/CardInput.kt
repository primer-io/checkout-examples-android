package io.primer.checkout.cobadged.checkout.data.model

import io.primer.android.ui.CardNetwork

data class CardInput(
    val cardNumber: String,
    val expiryDate: String,
    val expiryDateFormatted: String,
    val cvv: String,
    val cardHolderName: String?,
    val cardNetwork: CardNetwork.Type?
) {

    companion object {

        val EMPTY = CardInput("", "", "", "", null, null)
    }
}
