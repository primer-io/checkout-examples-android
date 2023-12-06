package io.primer.checkout.cobadged.checkout.ui.composable

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import io.primer.checkout.cobadged.R

@Composable
fun CardCvvInput(
    value: String,
    onValueChanged: (String) -> Unit,
    error: String?,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            onValueChanged(newValue.take(4))
        },
        modifier = modifier,
        label = { Text(text = stringResource(R.string.card_cvv)) },
        supportingText = { if (value.isNotBlank()) error?.let { Text(text = it) } },
        placeholder = { Text(text = stringResource(id = R.string.card_cvv_placeholder)) },
        isError = value.isNotBlank() && error.isNullOrBlank().not(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true
    )
}
