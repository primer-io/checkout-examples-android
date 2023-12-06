package io.primer.checkout.cobadged.result.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import io.primer.checkout.cobadged.R
import io.primer.checkout.cobadged.result.ui.CheckoutResult

@Composable
fun CheckoutResultScreenContent(checkoutResult: CheckoutResult, modifier: Modifier) {
    val screenPadding = Modifier.padding(
        horizontal = dimensionResource(id = R.dimen.horizontal_margin),
        vertical = dimensionResource(id = R.dimen.vertical_margin)
    )
    val commonModifier = modifier
        .fillMaxWidth()
        .then(screenPadding)

    Column(commonModifier.verticalScroll(rememberScrollState())) {
        Text(
            text = stringResource(
                id = when (checkoutResult) {
                    is CheckoutResult.Failed -> R.string.result_fail
                    is CheckoutResult.Success -> R.string.result_success
                }
            ),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacing_default)))

        OutlinedCard(modifier = Modifier.fillMaxWidth()) {
            when (checkoutResult) {
                is CheckoutResult.Success -> CheckoutResultInfo(checkoutResult.checkoutData)
                is CheckoutResult.Failed -> CheckoutResultError(checkoutResult)
            }
        }
    }
}
