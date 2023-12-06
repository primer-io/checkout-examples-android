package io.primer.checkout.cobadged.result.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.primer.checkout.cobadged.result.ui.composable.ResultScreenContent
import io.primer.checkout.cobadged.result.ui.composable.ResultTopAppBar

@Composable
fun CheckoutResultScreen(
    checkoutResult: CheckoutResult,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.onSurface),
        topBar = {
            ResultTopAppBar(onBack = onBack)
        }
    ) { contentPaddings ->
        ResultScreenContent(
            checkoutResult = checkoutResult,
            modifier = modifier.padding(contentPaddings)
        )
    }
}
