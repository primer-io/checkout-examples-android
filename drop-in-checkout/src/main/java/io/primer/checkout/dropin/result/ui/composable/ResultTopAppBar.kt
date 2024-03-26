package io.primer.checkout.dropin.result.ui.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.primer.checkout.dropin.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultTopAppBar(onBack: () -> Unit) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.result_title))
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.Clear, stringResource(id = R.string.menu_back))
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}
