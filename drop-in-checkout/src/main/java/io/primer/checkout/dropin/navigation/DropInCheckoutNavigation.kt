package io.primer.checkout.dropin.navigation

import android.net.Uri
import androidx.navigation.NavHostController
import io.primer.checkout.dropin.navigation.DropInCheckoutDestinations.RESULT_SCREEN_ROUTE
import io.primer.checkout.dropin.result.ui.CheckoutResult
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object DropInCheckoutDestinations {
    const val SETTINGS_ROUTE = "settings"
    const val RESULT_SCREEN_ROUTE = "checkoutResult"
}

object DropInCheckoutArguments {
    const val RESULT_ARGUMENT = "checkoutResult"
}

class DropInCheckoutNavigationActions(private val navController: NavHostController) {

    fun navigateToResultScreen(checkoutResult: CheckoutResult) {
        val json = Uri.encode(Json.encodeToString(checkoutResult))
        navController.navigate("$RESULT_SCREEN_ROUTE/$json")
    }
}
