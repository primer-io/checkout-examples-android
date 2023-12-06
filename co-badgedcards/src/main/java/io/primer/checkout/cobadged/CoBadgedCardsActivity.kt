package io.primer.checkout.cobadged

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import io.primer.checkout.cobadged.checkout.ui.theme.CheckoutExamplesTheme
import io.primer.checkout.cobadged.navigation.CoBadgedCardsNavGraph

@AndroidEntryPoint
class CoBadgedCardsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CheckoutExamplesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CoBadgedCardsNavGraph()
                }
            }
        }
    }
}
