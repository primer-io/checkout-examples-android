package io.primer.checkout.stripe.ach.configuration.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.primer.checkout.stripe.ach.R
import io.primer.checkout.stripe.ach.configuration.viewmodel.CheckoutConfigurationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutConfigurationScreen(
    onNavigateToCheckout: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CheckoutConfigurationViewModel = hiltViewModel()
) {
    val checkoutUiState by viewModel.checkoutUiState.collectAsState()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.onSurface),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.main_title)
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) { contentPaddings ->
        Column(
            modifier
                .verticalScroll(rememberScrollState())
                .padding(contentPaddings)
                .padding(dimensionResource(id = R.dimen.default_margin))
        ) {
            Text(
                text = stringResource(id = R.string.settings_title),
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacing_half)))

            Text(
                text = stringResource(id = R.string.settings_description),
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacing_double)))

            OutlinedTextField(
                value = checkoutUiState.clientTokenState.clientToken,
                onValueChange = viewModel::updateClientToken,
                modifier = modifier
                    .fillMaxWidth()
                    .height(height = dimensionResource(id = R.dimen.client_token_field_min_height)),
                label = { Text(text = stringResource(id = R.string.client_token)) },
                placeholder = { Text(text = stringResource(id = R.string.client_token)) }
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacing_default)))

            Divider(color = colorResource(id = R.color.black), thickness = 0.5.dp)

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacing_default)))

            OutlinedTextField(
                value = checkoutUiState.serverUrl,
                onValueChange = viewModel::updateServerUrl,
                modifier = modifier.fillMaxWidth(),
                label = { Text(text = stringResource(id = R.string.server_url)) },
                placeholder = { Text(text = stringResource(id = R.string.server_url_hint)) }
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacing_half)))

            Button(
                onClick = { viewModel.requestNewClientToken() },
                modifier = modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(),
                enabled = checkoutUiState.serverUrl.isNotBlank()
            ) {
                Text(text = stringResource(id = R.string.request_client_token))
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacing_default)))

            Button(
                onClick = {
                    onNavigateToCheckout.invoke(checkoutUiState.clientTokenState.clientToken)
                },
                modifier = modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(),
                enabled = checkoutUiState.clientTokenState.isValid
            ) {
                Text(text = stringResource(id = R.string.open_checkout))
            }

            checkoutUiState.errorMessage?.let { errorMessage ->
                Column {
                    Spacer(
                        modifier = Modifier.height(
                            dimensionResource(id = R.dimen.spacing_default)
                        )
                    )

                    OutlinedCard(
                        modifier = modifier.fillMaxWidth(),
                        border = BorderStroke(0.5.dp, color = MaterialTheme.colorScheme.error)
                    ) {
                        Text(
                            text = errorMessage,
                            modifier = modifier.padding(
                                dimensionResource(id = R.dimen.default_margin)
                            )
                        )
                    }
                }
            }
        }
    }
}
