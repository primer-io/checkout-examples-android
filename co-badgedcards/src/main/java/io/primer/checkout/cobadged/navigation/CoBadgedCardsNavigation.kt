package io.primer.checkout.cobadged.navigation

import android.net.Uri
import androidx.navigation.NavHostController
import io.primer.checkout.cobadged.navigation.CoBadgedCardsDestinations.PAYMENT_CARD_ROUTE
import io.primer.checkout.cobadged.navigation.CoBadgedCardsDestinations.RESULT_SCREEN_ROUTE
import io.primer.checkout.cobadged.result.ui.CheckoutResult
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object CoBadgedCardsDestinations {
    const val SETTINGS_ROUTE = "settings"
    const val PAYMENT_CARD_ROUTE = "paymentCard"
    const val RESULT_SCREEN_ROUTE = "checkoutResult"
}

object CoBadgedCardsArguments {
    const val CLIENT_TOKEN_ARGUMENT = "clientToken"
    const val RESULT_ARGUMENT = "checkoutResult"
}

class CoBadgedCardsNavigationActions(private val navController: NavHostController) {

    fun navigateToCheckout(clientToken: String) {
        navController.navigate("$PAYMENT_CARD_ROUTE/$clientToken") {
            restoreState = true
        }
    }

    fun navigateToResultScreen(checkoutResult: CheckoutResult) {
        val json = Uri.encode(Json.encodeToString(checkoutResult))
        navController.navigate("$RESULT_SCREEN_ROUTE/$json")
    }
}
