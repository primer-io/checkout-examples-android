package io.primer.checkout.dropin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import io.primer.checkout.dropin.checkout.ui.theme.CheckoutExamplesTheme
import io.primer.checkout.dropin.navigation.DropInCheckoutNavGraph

@AndroidEntryPoint
class DropInCheckoutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CheckoutExamplesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DropInCheckoutNavGraph()
                }
            }
        }
    }
}
