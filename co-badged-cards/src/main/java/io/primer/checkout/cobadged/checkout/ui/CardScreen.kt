package io.primer.checkout.cobadged.checkout.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import io.primer.checkout.cobadged.checkout.ui.composable.CardTopAppBar
import io.primer.checkout.cobadged.checkout.ui.composable.CardView
import io.primer.checkout.cobadged.checkout.ui.composable.IndeterminateCircularIndicator
import io.primer.checkout.cobadged.checkout.viewmodel.CardFormUiState
import io.primer.checkout.cobadged.checkout.viewmodel.CardInputViewModel
import io.primer.checkout.cobadged.result.ui.CheckoutResult

@Composable
fun CardScreen(
    clientToken: String,
    onCheckoutResult: (CheckoutResult) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CardInputViewModel = hiltViewModel()
) {
    val cardFormUiState by viewModel.cardFormUiState.collectAsState()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.onSurface),
        topBar = {
            CardTopAppBar(onBack = onBack)
        }
    ) { contentPaddings ->
        when (val state = cardFormUiState) {
            is CardFormUiState.CheckoutCompleted -> onCheckoutResult(state.checkoutResult)
            is CardFormUiState.CheckoutError -> onCheckoutResult(state.checkoutResult)
            CardFormUiState.Initializing -> IndeterminateCircularIndicator(
                modifier
            )
            CardFormUiState.Submitted -> IndeterminateCircularIndicator(
                modifier
            )

            is CardFormUiState.ShowCardForm -> CardView(
                modifier.padding(contentPaddings),
                state.showCardholderName,
                state.allowedCardNetworks,
                viewModel
            )
        }
    }

    LaunchedEffect(key1 = clientToken, block = {
        viewModel.startCheckout(clientToken)
    })
}
