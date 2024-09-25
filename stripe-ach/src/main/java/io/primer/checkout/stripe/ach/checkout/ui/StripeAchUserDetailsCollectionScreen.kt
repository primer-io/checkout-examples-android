package io.primer.checkout.stripe.ach.checkout.ui

import androidx.activity.result.ActivityResultRegistry
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
import androidx.compose.ui.unit.dp
import io.primer.checkout.stripe.ach.checkout.ui.composable.IndeterminateCircularIndicator
import io.primer.checkout.stripe.ach.checkout.ui.composable.StripeAchMandateDialog
import io.primer.checkout.stripe.ach.checkout.ui.composable.StripeAchTopAppBar
import io.primer.checkout.stripe.ach.checkout.ui.composable.UserDetailsCollectionView
import io.primer.checkout.stripe.ach.checkout.viewmodel.StripeAchUiState
import io.primer.checkout.stripe.ach.checkout.viewmodel.StripeAchViewModel
import io.primer.checkout.stripe.ach.ui.CheckoutResult

@Composable
fun StripeAchUserDetailsCollectionScreen(
    clientToken: String,
    modifier: Modifier = Modifier,
    onCheckoutResult: (CheckoutResult) -> Unit,
    onBack: () -> Unit,
    viewModel: StripeAchViewModel,
    activityResultRegistry: ActivityResultRegistry
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.onSurface),
        topBar = {
            StripeAchTopAppBar(onBack = onBack)
        }
    ) { contentPaddings ->
        val uiState by viewModel.uiState.collectAsState()
        val validationUiState by viewModel.validationUiState.collectAsState()

        when (val state = uiState) {
            is StripeAchUiState.CheckoutCompleted -> onCheckoutResult(state.checkoutResult)

            is StripeAchUiState.CheckoutError -> onCheckoutResult(state.checkoutResult)

            StripeAchUiState.Initializing -> IndeterminateCircularIndicator(modifier)

            is StripeAchUiState.UserDetailsRetrieved -> UserDetailsCollectionView(
                firstName = state.firstName,
                isFirstNameValid = validationUiState.isFirstNameValid,
                onFirstNameChange = viewModel::onSetFirstName,
                lastName = state.lastName,
                isLastNameValid = validationUiState.isLastNameValid,
                onLastNameChange = viewModel::onSetLastName,
                emailAddress = state.emailAddress,
                isEmailAddressValid = validationUiState.isEmailAddressValid,
                onEmailAddressChange = viewModel::onSetEmailAddress,
                onSubmitClick = viewModel::submitUserDetails,
                modifier = Modifier
                    .padding(contentPaddings)
                    .fillMaxSize()
                    .padding(16.dp)
            )

            is StripeAchUiState.UserDetailsCollected -> IndeterminateCircularIndicator(modifier)

            StripeAchUiState.DisplayMandate -> StripeAchMandateDialog(
                onDeclineMandate = viewModel::declineMandate,
                onAcceptMandate = viewModel::acceptMandate
            )
        }
    }

    LaunchedEffect(clientToken, activityResultRegistry) {
        viewModel.startCheckout(clientToken, activityResultRegistry)
    }
}
