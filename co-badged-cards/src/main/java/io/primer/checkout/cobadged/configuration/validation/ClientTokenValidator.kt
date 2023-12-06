package io.primer.checkout.cobadged.configuration.validation

interface ClientTokenValidator {

    fun validate(clientToken: String): Boolean
}

class SimpleClientTokenValidator : ClientTokenValidator {
    override fun validate(clientToken: String): Boolean {
        return REGEX.matches(clientToken)
    }

    private companion object {

        val REGEX = Regex("^[\\w-]+\\.[\\w-]+\\.[\\w-]+$")
    }
}
