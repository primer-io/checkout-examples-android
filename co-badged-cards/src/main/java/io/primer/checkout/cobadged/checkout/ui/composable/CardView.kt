package io.primer.checkout.cobadged.checkout.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import io.primer.checkout.cobadged.R
import io.primer.checkout.cobadged.checkout.data.model.CardNetworkMetadata
import io.primer.checkout.cobadged.checkout.viewmodel.CardInputViewModel

@Composable
fun CardView(
    modifier: Modifier = Modifier,
    showCardholderName: Boolean,
    allowedCardNetworks: List<CardNetworkMetadata>,
    viewModel: CardInputViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current

    val input by viewModel.cardInput.collectAsState()
    val validation by viewModel.cardValidationEvents.collectAsState(null)
    val cardNetworksState by viewModel.cardNetworksStateEvents.collectAsState(null)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .navigationBarsPadding()
            .padding(horizontal = dimensionResource(id = R.dimen.horizontal_margin))
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center
    ) {
        CardForm(
            input = input,
            showCardholderName = showCardholderName,
            allowedCardNetworks = allowedCardNetworks,
            validationErrors = validation?.validationErrors,
            cardNetworksState = cardNetworksState,
            onCardInputChanged = { cardInput ->
                viewModel.onCardInputChanged(cardInput)
            },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            enabled = validation?.isValid ?: false,
            onClick = {
                viewModel.submitData().also {
                    focusManager.clearFocus()
                }
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = dimensionResource(id = R.dimen.horizontal_margin))
                .fillMaxWidth(),
            content = { Text(text = stringResource(id = R.string.pay)) }
        )
    }
}
