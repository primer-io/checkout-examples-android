package io.primer.checkout.dropin.checkout.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import io.primer.android.domain.PrimerCheckoutData
import io.primer.android.domain.error.models.PrimerError
import io.primer.checkout.dropin.R
import javax.inject.Inject

interface ErrorMapper {
    fun toErrorMessage(
        error: PrimerError,
        checkoutData: PrimerCheckoutData?
    ): String
}

class DefaultErrorMapper @Inject constructor(
    @ApplicationContext private val context: Context
) : ErrorMapper {
    override fun toErrorMessage(
        error: PrimerError,
        checkoutData: PrimerCheckoutData?
    ): String {
        // parse error and checkout data and build the appropriate error message
        return context.getString(R.string.error_handler_custom_message)
    }
}
