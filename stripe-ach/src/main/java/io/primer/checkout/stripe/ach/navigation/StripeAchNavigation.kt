package io.primer.checkout.stripe.ach.navigation

import android.net.Uri
import androidx.navigation.NavHostController
import io.primer.checkout.stripe.ach.navigation.StripeAchDestinations.USER_DETAILS_COLLECTION_ROUTE
import io.primer.checkout.stripe.ach.navigation.StripeAchDestinations.RESULT_SCREEN_ROUTE
import io.primer.checkout.stripe.ach.ui.CheckoutResult
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object StripeAchDestinations {
    const val SETTINGS_ROUTE = "settings"
    const val USER_DETAILS_COLLECTION_ROUTE = "userDetailsCollection"
    const val RESULT_SCREEN_ROUTE = "checkoutResult"
}

object StripeAchArguments {
    const val CLIENT_TOKEN_ARGUMENT = "clientToken"
    const val RESULT_ARGUMENT = "checkoutResult"
}

class StripeAchNavigationActions(private val navController: NavHostController) {

    fun navigateToUserDetailsCollection(clientToken: String) {
        navController.navigate("$USER_DETAILS_COLLECTION_ROUTE/$clientToken") {
            restoreState = true
        }
    }

    fun navigateToResultScreen(checkoutResult: CheckoutResult) {
        val json = Uri.encode(Json.encodeToString(checkoutResult))
        navController.navigate("$RESULT_SCREEN_ROUTE/$json")
    }
}
