package io.primer.checkout.dropin.checkout.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import io.primer.checkout.dropin.checkout.ui.composable.DropInTopAppBar
import io.primer.checkout.dropin.checkout.ui.composable.IndeterminateCircularIndicator
import io.primer.checkout.dropin.checkout.viewmodel.DropInUiState
import io.primer.checkout.dropin.checkout.viewmodel.DropInViewModel
import io.primer.checkout.dropin.result.ui.CheckoutResult

@Composable
fun DropInScreen(
    clientToken: String,
    onCheckoutResult: (CheckoutResult) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DropInViewModel = hiltViewModel()
) {
    val dropInUiState by viewModel.dropInUiState.collectAsState()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.onSurface),
        topBar = {
            DropInTopAppBar(onBack = onBack)
        }
    ) { _ ->
        when (val state = dropInUiState) {
            is DropInUiState.CheckoutCompleted -> onCheckoutResult(state.checkoutResult)
            is DropInUiState.CheckoutError -> onCheckoutResult(state.checkoutResult)
            DropInUiState.Initializing -> IndeterminateCircularIndicator(modifier)
        }
    }

    LaunchedEffect(key1 = clientToken, block = {
        viewModel.startCheckout(clientToken)
    })
}
