package io.primer.checkout.cobadged.checkout.data.model

data class ValidationErrors(
    val cardNumber: String?,
    val cardExpiryDate: String?,
    val cardCvv: String?,
    val cardholderName: String?
)

data class CardValidation(val isValid: Boolean, val validationErrors: ValidationErrors)
