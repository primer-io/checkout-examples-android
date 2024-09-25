package io.primer.checkout.stripe.ach.navigation

import android.os.Bundle
import androidx.activity.result.ActivityResultRegistry
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.primer.android.components.presentation.paymentMethods.nativeUi.stripe.ach.StripeAchUserDetailsComponent
import io.primer.checkout.stripe.ach.checkout.ui.StripeAchUserDetailsCollectionScreen
import io.primer.checkout.stripe.ach.checkout.viewmodel.StripeAchViewModel
import io.primer.checkout.stripe.ach.configuration.extensions.getParcelableCompat
import io.primer.checkout.stripe.ach.configuration.ui.CheckoutConfigurationScreen
import io.primer.checkout.stripe.ach.navigation.StripeAchArguments.CLIENT_TOKEN_ARGUMENT
import io.primer.checkout.stripe.ach.navigation.StripeAchArguments.RESULT_ARGUMENT
import io.primer.checkout.stripe.ach.navigation.StripeAchDestinations.RESULT_SCREEN_ROUTE
import io.primer.checkout.stripe.ach.navigation.StripeAchDestinations.SETTINGS_ROUTE
import io.primer.checkout.stripe.ach.navigation.StripeAchDestinations.USER_DETAILS_COLLECTION_ROUTE
import io.primer.checkout.stripe.ach.ui.CheckoutResult
import io.primer.checkout.stripe.ach.ui.CheckoutResultScreen
import kotlinx.serialization.json.Json

@Composable
fun StripeAchNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = SETTINGS_ROUTE,
    componentProvider: () -> StripeAchUserDetailsComponent,
    activityResultRegistry: ActivityResultRegistry,
    navActions: StripeAchNavigationActions = remember(navController) {
        StripeAchNavigationActions(navController)
    }
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(SETTINGS_ROUTE) {
            CheckoutConfigurationScreen(onNavigateToCheckout = { clientToken ->
                navActions.navigateToUserDetailsCollection(clientToken)
            })
        }

        composable(
            "$USER_DETAILS_COLLECTION_ROUTE/{$CLIENT_TOKEN_ARGUMENT}",
            arguments = listOf(navArgument(CLIENT_TOKEN_ARGUMENT) { type = NavType.StringType })
        ) { backStackEntry ->
            StripeAchUserDetailsCollectionScreen(
                requireNotNull(
                    backStackEntry.arguments?.getString(CLIENT_TOKEN_ARGUMENT)
                ),
                onBack = {
                    navController.popBackStack()
                },
                onCheckoutResult = { checkoutResult ->
                    navActions.navigateToResultScreen(checkoutResult)
                },
                viewModel = hiltViewModel(
                    creationCallback = { factory: StripeAchViewModel.ViewModelFactory ->
                        val component by lazy { componentProvider() }

                        factory.create(
                            startComponent = { component.start() },
                            step = { component.componentStep },
                            error = { component.componentError },
                            validation = { component.componentValidationStatus },
                            updateCollectedData = { component.updateCollectedData(it) },
                            submitComponent = { component.submit() }
                        )
                    }
                ),
                activityResultRegistry = activityResultRegistry
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
                currentBackStackEntry.arguments?.getParcelableCompat<CheckoutResult>(RESULT_ARGUMENT)
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
