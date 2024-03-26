package io.primer.checkout.dropin.result.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.primer.checkout.dropin.result.ui.composable.CheckoutResultScreenContent
import io.primer.checkout.dropin.result.ui.composable.ResultTopAppBar

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
        CheckoutResultScreenContent(
            checkoutResult = checkoutResult,
            modifier = modifier.padding(contentPaddings)
        )
    }
}
