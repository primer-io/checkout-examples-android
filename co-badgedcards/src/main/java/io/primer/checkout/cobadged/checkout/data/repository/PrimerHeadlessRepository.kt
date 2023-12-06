package io.primer.checkout.cobadged.checkout.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import io.primer.android.components.PrimerHeadlessUniversalCheckout
import io.primer.android.components.PrimerHeadlessUniversalCheckoutListener
import io.primer.android.components.domain.core.models.PrimerHeadlessUniversalCheckoutPaymentMethod
import io.primer.android.core.logging.PrimerLogLevel
import io.primer.android.core.logging.PrimerLogging
import io.primer.android.domain.PrimerCheckoutData
import io.primer.android.domain.error.models.PrimerError
import io.primer.checkout.cobadged.BuildConfig
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

interface PrimerHeadlessRepository {

    fun start(clientToken: String)

    val primerHeadlessEvents: Flow<PrimerHeadlessEvent>
}

sealed class PrimerHeadlessEvent {

    data class AvailablePaymentMethodsLoaded(
        val paymentMethods: List<PrimerHeadlessUniversalCheckoutPaymentMethod>
    ) : PrimerHeadlessEvent()

    data class CheckoutFailed(val errorMessage: String, val checkoutData: PrimerCheckoutData?) :
        PrimerHeadlessEvent()

    data class CheckoutCompleted(val checkoutData: PrimerCheckoutData) : PrimerHeadlessEvent()
}

class DefaultPrimerHeadlessRepository @Inject constructor(
    @ApplicationContext private val context: Context
) : PrimerHeadlessRepository {

    private val headlessUniversalCheckout = PrimerHeadlessUniversalCheckout.current
    override val primerHeadlessEvents: Flow<PrimerHeadlessEvent> = callbackFlow {
        headlessUniversalCheckout.setCheckoutListener(
            object : PrimerHeadlessUniversalCheckoutListener {

                override fun onAvailablePaymentMethodsLoaded(
                    paymentMethods: List<PrimerHeadlessUniversalCheckoutPaymentMethod>
                ) {
                    trySend(PrimerHeadlessEvent.AvailablePaymentMethodsLoaded(paymentMethods))
                }

                override fun onFailed(error: PrimerError, checkoutData: PrimerCheckoutData?) {
                    trySend(
                        PrimerHeadlessEvent.CheckoutFailed(
                            error.description,
                            checkoutData
                        )
                    )
                }

                override fun onCheckoutCompleted(checkoutData: PrimerCheckoutData) {
                    trySend(PrimerHeadlessEvent.CheckoutCompleted(checkoutData))
                }
            }
        )

        awaitClose {
            headlessUniversalCheckout.cleanup()
        }
    }

    override fun start(clientToken: String) {
        PrimerLogging.logger.logLevel =
            if (BuildConfig.DEBUG) PrimerLogLevel.DEBUG else PrimerLogLevel.NONE
        headlessUniversalCheckout.start(context, clientToken)
    }

    companion object {

        const val CARD_PAYMENT_METHOD_TYPE = "PAYMENT_CARD"
    }
}
