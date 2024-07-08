package io.primer.checkout.stripe.ach.checkout.ui.composable

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.primer.checkout.stripe.ach.R

@Composable
fun StripeAchMandateDialog(
    onDeclineMandate: () -> Unit,
    onAcceptMandate: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDeclineMandate,
        confirmButton = {
            TextButton(onClick = onAcceptMandate) {
                Text(text = stringResource(id = R.string.stripe_ach_mandate_dialog_accept_text))
            }
        },
        dismissButton = {
            TextButton(onClick = onDeclineMandate) {
                Text(text = stringResource(id = R.string.stripe_ach_mandate_dialog_decline_text))
            }
        },
        title = {
            Text(text = stringResource(id = R.string.stripe_ach_mandate_dialog_title))
        },
        text = {
            Text(text = stringResource(id = R.string.stripe_ach_mandate_dialog_text))
        },
        modifier = modifier
    )
}
