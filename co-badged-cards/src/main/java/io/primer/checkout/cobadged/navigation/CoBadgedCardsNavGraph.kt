package io.primer.checkout.cobadged.navigation

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.primer.checkout.cobadged.navigation.CoBadgedCardsDestinations.PAYMENT_CARD_ROUTE
import io.primer.checkout.cobadged.navigation.CoBadgedCardsArguments.CLIENT_TOKEN_ARGUMENT
import io.primer.checkout.cobadged.navigation.CoBadgedCardsArguments.RESULT_ARGUMENT
import io.primer.checkout.cobadged.navigation.CoBadgedCardsDestinations.RESULT_SCREEN_ROUTE
import io.primer.checkout.cobadged.checkout.ui.CardScreen
import io.primer.checkout.cobadged.configuration.extensions.parcelable
import io.primer.checkout.cobadged.configuration.ui.CheckoutConfigurationScreen
import io.primer.checkout.cobadged.navigation.CoBadgedCardsDestinations.SETTINGS_ROUTE
import io.primer.checkout.cobadged.result.ui.CheckoutResult
import io.primer.checkout.cobadged.result.ui.CheckoutResultScreen
import kotlinx.serialization.json.Json

@Composable
fun CoBadgedCardsNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = SETTINGS_ROUTE,
    navActions: CoBadgedCardsNavigationActions = remember(navController) {
        CoBadgedCardsNavigationActions(navController)
    }
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(SETTINGS_ROUTE) {
            CheckoutConfigurationScreen(onNavigateToCheckout = { clientToken ->
                navActions.navigateToCheckout(
                    clientToken
                )
            })
        }

        composable(
            "$PAYMENT_CARD_ROUTE/{$CLIENT_TOKEN_ARGUMENT}",
            arguments = listOf(navArgument(CLIENT_TOKEN_ARGUMENT) { type = NavType.StringType })
        ) { backStackEntry ->
            CardScreen(
                requireNotNull(
                    backStackEntry.arguments?.getString(CLIENT_TOKEN_ARGUMENT)
                ),
                onBack = {
                    navController.popBackStack()
                },
                onCheckoutResult = { checkoutResult ->
                    navActions.navigateToResultScreen(
                        checkoutResult
                    )
                }
            )
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
                currentBackStackEntry.arguments?.parcelable<CheckoutResult>(
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
        return bundle.parcelable(key)
    }

    override fun parseValue(value: String): CheckoutResult {
        return Json.decodeFromString(value)
    }

    override fun put(bundle: Bundle, key: String, value: CheckoutResult) {
        bundle.putParcelable(key, value)
    }
}
