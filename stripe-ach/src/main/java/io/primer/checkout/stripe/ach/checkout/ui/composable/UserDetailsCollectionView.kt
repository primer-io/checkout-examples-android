package io.primer.checkout.stripe.ach.checkout.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.primer.checkout.stripe.ach.R

@Composable
fun UserDetailsCollectionView(
    firstName: String,
    isFirstNameValid: Boolean,
    onFirstNameChange: (String) -> Unit,
    lastName: String,
    isLastNameValid: Boolean,
    onLastNameChange: (String) -> Unit,
    emailAddress: String,
    isEmailAddressValid: Boolean,
    onEmailAddressChange: (String) -> Unit,
    onSubmitClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        ValidatedTextField(
            value = firstName,
            label = stringResource(id = R.string.stripe_ach_first_name_text),
            onValueChange = onFirstNameChange,
            isValid = isFirstNameValid,
            errorText = stringResource(id = R.string.stripe_ach_first_name_error_text)
        )
        VerticalSpace()
        ValidatedTextField(
            value = lastName,
            label = stringResource(id = R.string.stripe_ach_last_name_text),
            onValueChange = onLastNameChange,
            isValid = isLastNameValid,
            errorText = stringResource(id = R.string.stripe_ach_last_name_error_text)
        )
        VerticalSpace()
        ValidatedTextField(
            value = emailAddress,
            label = stringResource(id = R.string.stripe_ach_email_address_text),
            onValueChange = onEmailAddressChange,
            isValid = isEmailAddressValid,
            errorText = stringResource(id = R.string.stripe_ach_email_address_error_text)
        )
        val focusManager = LocalFocusManager.current
        var isSubmitted by remember { mutableStateOf(false) }
        Button(
            onClick = {
                isSubmitted = true
                focusManager.clearFocus()
                onSubmitClick()
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 24.dp),
            enabled = isFirstNameValid && isLastNameValid && isEmailAddressValid && !isSubmitted
        ) {
            Text(text = stringResource(id = R.string.stripe_ach_submit_text))
        }
    }
}

@Preview
@Composable
fun UserDetailsCollectionViewPreview() {
    UserDetailsCollectionView(
        firstName = "John",
        isFirstNameValid = true,
        onFirstNameChange = {},
        lastName = "",
        isLastNameValid = false,
        onLastNameChange = {},
        emailAddress = "",
        isEmailAddressValid = false,
        onEmailAddressChange = {},
        onSubmitClick = {}
    )
}

@Composable
private fun ValidatedTextField(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    isValid: Boolean,
    errorText: String,
    modifier: Modifier = Modifier
) {
    var wrappedValue by remember { mutableStateOf(value) }
    OutlinedTextField(
        value = wrappedValue,
        onValueChange = {
            wrappedValue = it
            onValueChange(it)
        },
        modifier = modifier.fillMaxWidth(),
        label = {
            Text(text = label)
        },
        isError = !isValid,
        supportingText = {
            if (!isValid) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = errorText,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    )
}

@Composable
private fun VerticalSpace() {
    Spacer(Modifier.height(8.dp))
}
