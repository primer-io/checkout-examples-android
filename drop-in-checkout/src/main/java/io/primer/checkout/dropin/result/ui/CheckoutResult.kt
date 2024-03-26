package io.primer.checkout.dropin.result.ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
sealed class CheckoutResult : Parcelable {

    @Serializable
    data class Success(val checkoutData: CheckoutData) : CheckoutResult()

    @Serializable
    data class Failed(val error: String, val checkoutData: CheckoutData?) :
        CheckoutResult()
}

@Parcelize
@Serializable
data class CheckoutData(val orderId: String, val paymentId: String) : Parcelable
