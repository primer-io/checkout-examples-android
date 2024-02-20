package io.primer.checkout.cobadged.checkout.data.extension

import io.primer.android.components.domain.error.PrimerInputValidationError

fun List<PrimerInputValidationError>.findErrorById(errorId: String) =
    find { error -> error.errorId == errorId }?.description
