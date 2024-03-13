package io.primer.checkout.dropin.result.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import io.primer.checkout.dropin.R
import io.primer.checkout.dropin.result.ui.CheckoutData
import io.primer.checkout.dropin.result.ui.CheckoutResult

@Composable
fun CheckoutResultInfo(checkoutData: CheckoutData?, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        checkoutData?.let { checkoutData ->
            Text(
                text = stringResource(
                    id = R.string.result_order_id_placeholder,
                    checkoutData.orderId
                ),
                modifier = Modifier.padding(
                    dimensionResource(id = R.dimen.default_margin)
                )
            )

            Text(
                text = stringResource(
                    id = R.string.result_payment_id_placeholder,
                    checkoutData.paymentId
                ),
                modifier = Modifier.padding(
                    dimensionResource(id = R.dimen.default_margin)
                )
            )
        }
    }
}

@Composable
fun CheckoutResultError(checkoutResult: CheckoutResult.Failed, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = checkoutResult.error,
            modifier = Modifier.padding(
                dimensionResource(id = R.dimen.default_margin)
            )
        )

        CheckoutResultInfo(checkoutResult.checkoutData, modifier)
    }
}
