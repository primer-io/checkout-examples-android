package io.primer.checkout.cobadged.checkout.ui.formatting

enum class CardFormat(val mask: String, val cvvLength: Int = 3) {

    VISA("#### #### #### ####"),
    MASTERCARD("#### #### #### ####"),
    MAESTRO("#### #### #### ####"),
    CARTES_BANCAIRES("#### #### #### ####"),
    AMEX("#### ###### #####", 4),
    DINERS_CLUB("#### ###### ####"),
    DISCOVER("#### #### #### ####"),
    JCB("#### #### #### #######"),
    UNIONPAY("#### #### #### #######"),
    MIR("#### #### #### #######"),
    UNKNOWN("#### #### #### ####");

    fun cardNumberMaxLength() = mask.filter { it == '#' }.length
}
