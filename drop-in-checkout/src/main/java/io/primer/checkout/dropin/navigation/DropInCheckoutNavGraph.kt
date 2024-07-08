package io.primer.checkout.dropin.navigation

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.primer.checkout.dropin.configuration.extensions.getParcelableCompat
import io.primer.checkout.dropin.configuration.ui.CheckoutConfigurationScreen
import io.primer.checkout.dropin.navigation.DropInCheckoutArguments.RESULT_ARGUMENT
import io.primer.checkout.dropin.navigation.DropInCheckoutDestinations.RESULT_SCREEN_ROUTE
import io.primer.checkout.dropin.navigation.DropInCheckoutDestinations.SETTINGS_ROUTE
import io.primer.checkout.dropin.result.ui.CheckoutResult
import io.primer.checkout.dropin.result.ui.CheckoutResultScreen
import kotlinx.serialization.json.Json

@Composable
fun DropInCheckoutNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = SETTINGS_ROUTE,
    navActions: DropInCheckoutNavigationActions = remember(navController) {
        DropInCheckoutNavigationActions(navController)
    }
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(SETTINGS_ROUTE) {
            CheckoutConfigurationScreen(onCheckoutResult = { checkoutResult ->
                navActions.navigateToResultScreen(
                    checkoutResult
                )
            })
        }

        composable(
            "$RESULT_SCREEN_ROUTE/{$RESULT_ARGUMENT}",
            arguments = listOf(
                navArgument(RESULT_ARGUMENT) {
                    type = CheckoutResultParamType()
                }
            )
        ) { currentBackStackEntry ->
            val checkoutResult = requireNotNull(
                currentBackStackEntry.arguments?.getParcelableCompat<CheckoutResult>(
                    RESULT_ARGUMENT
                )
            )

            CheckoutResultScreen(
                checkoutResult,
                onBack = {
                    navController.popBackStack(
                        SETTINGS_ROUTE,
                        false
                    )
                }
            )
        }
    }
}

class CheckoutResultParamType : NavType<CheckoutResult>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): CheckoutResult? {
        return bundle.getParcelableCompat(key)
    }

    override fun parseValue(value: String): CheckoutResult {
        return Json.decodeFromString(value)
    }

    override fun put(bundle: Bundle, key: String, value: CheckoutResult) {
        bundle.putParcelable(key, value)
    }
}
