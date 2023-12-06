package io.primer.checkout.cobadged.checkout.ui.composable

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import io.primer.checkout.cobadged.R
import io.primer.checkout.cobadged.checkout.ui.formatting.MaskVisualTransformation

@Composable
fun CardExpiryDateInput(
    value: String,
    onValueChanged: (String, String) -> Unit,
    error: String?,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            val expiryDate = newValue.take(ExpiryDateFormat.MAX_LENGTH)
            onValueChanged(
                expiryDate,
                ExpiryDateFormat.visualTransformation.filter(
                    AnnotatedString(expiryDate)
                ).text.text
            )
        },
        modifier = modifier,
        label = { Text(text = stringResource(R.string.card_expiry)) },
        supportingText = { if (value.isNotBlank()) error?.let { Text(text = it) } },
        placeholder = { Text(text = stringResource(id = R.string.card_expiry_placeholder)) },
        isError = value.isNotBlank() && error.isNullOrBlank().not(),
        visualTransformation = ExpiryDateFormat.visualTransformation,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true
    )
}

private object ExpiryDateFormat {

    const val EXPIRY_DATE_MASK = "##/##"
    const val MAX_LENGTH = 4
    val visualTransformation = MaskVisualTransformation(EXPIRY_DATE_MASK)
}
