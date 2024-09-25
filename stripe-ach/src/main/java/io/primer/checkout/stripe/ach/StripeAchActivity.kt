package io.primer.checkout.stripe.ach

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import io.primer.android.components.manager.ach.PrimerHeadlessUniversalCheckoutAchManager
import io.primer.android.components.presentation.paymentMethods.nativeUi.stripe.ach.StripeAchUserDetailsComponent
import io.primer.checkout.stripe.ach.checkout.data.repository.DefaultPrimerHeadlessRepository.Companion.STRIPE_ACH_PAYMENT_METHOD_TYPE
import io.primer.checkout.stripe.ach.checkout.ui.theme.CheckoutExamplesTheme
import io.primer.checkout.stripe.ach.navigation.StripeAchNavGraph

@AndroidEntryPoint
class StripeAchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CheckoutExamplesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Wrapped in a lambda because headless checkout has to be initialized before we can init the component
                    val componentProvider = {
                        PrimerHeadlessUniversalCheckoutAchManager(
                            this@StripeAchActivity
                        ).provide<StripeAchUserDetailsComponent>(STRIPE_ACH_PAYMENT_METHOD_TYPE)
                    }

                    StripeAchNavGraph(
                        componentProvider = componentProvider,
                        activityResultRegistry = activityResultRegistry
                    )
                }
            }
        }
    }
}
