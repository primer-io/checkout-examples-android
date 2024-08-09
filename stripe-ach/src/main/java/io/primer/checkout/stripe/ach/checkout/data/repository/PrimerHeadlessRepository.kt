package io.primer.checkout.stripe.ach.checkout.data.repository

import android.content.Context
import androidx.activity.result.ActivityResultRegistry
import dagger.hilt.android.qualifiers.ApplicationContext
import io.primer.android.components.PrimerHeadlessUniversalCheckout
import io.primer.android.components.PrimerHeadlessUniversalCheckoutListener
import io.primer.android.components.domain.core.models.PrimerHeadlessUniversalCheckoutPaymentMethod
import io.primer.android.core.logging.PrimerLogLevel
import io.primer.android.core.logging.PrimerLogging
import io.primer.android.data.settings.PrimerPaymentMethodOptions
import io.primer.android.data.settings.PrimerSettings
import io.primer.android.data.settings.PrimerStripeOptions
import io.primer.android.domain.PrimerCheckoutData
import io.primer.android.domain.error.models.PrimerError
import io.primer.android.domain.payments.additionalInfo.AchAdditionalInfo
import io.primer.android.domain.payments.additionalInfo.PrimerCheckoutAdditionalInfo
import io.primer.checkout.stripe.ach.BuildConfig
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

interface PrimerHeadlessRepository {

    fun start(clientToken: String, activityResultRegistry: ActivityResultRegistry)

    val primerHeadlessEvents: Flow<PrimerHeadlessEvent>
}

sealed interface PrimerHeadlessEvent {

    data class AvailablePaymentMethodsLoaded(
        val paymentMethods: List<PrimerHeadlessUniversalCheckoutPaymentMethod>
    ) : PrimerHeadlessEvent

    data class CheckoutFailed(val errorMessage: String, val checkoutData: PrimerCheckoutData?) :
        PrimerHeadlessEvent

    data class CheckoutCompleted(val checkoutData: PrimerCheckoutData) : PrimerHeadlessEvent

    data class DisplayMandateStripeAchMandate(
        val onAcceptMandate: suspend () -> Unit,
        val onDeclineMandate: suspend () -> Unit
    ) : PrimerHeadlessEvent
}

class DefaultPrimerHeadlessRepository @Inject constructor(
    @ApplicationContext private val context: Context
) : PrimerHeadlessRepository {
    private var activityResultRegistry: ActivityResultRegistry? = null

    private val headlessUniversalCheckout = PrimerHeadlessUniversalCheckout.current
    override val primerHeadlessEvents: Flow<PrimerHeadlessEvent> = callbackFlow {
        headlessUniversalCheckout.setCheckoutListener(
            object : PrimerHeadlessUniversalCheckoutListener {

                override fun onAvailablePaymentMethodsLoaded(
                    paymentMethods: List<PrimerHeadlessUniversalCheckoutPaymentMethod>
                ) {
                    trySend(PrimerHeadlessEvent.AvailablePaymentMethodsLoaded(paymentMethods))
                }

                override fun onCheckoutCompleted(checkoutData: PrimerCheckoutData) {
                    trySend(PrimerHeadlessEvent.CheckoutCompleted(checkoutData))
                }

                override fun onFailed(error: PrimerError, checkoutData: PrimerCheckoutData?) {
                    trySend(
                        PrimerHeadlessEvent.CheckoutFailed(
                            error.description,
                            checkoutData
                        )
                    )
                }

                override fun onCheckoutAdditionalInfoReceived(additionalInfo: PrimerCheckoutAdditionalInfo) {
                    when (additionalInfo) {
                        is AchAdditionalInfo.ProvideActivityResultRegistry -> {
                            additionalInfo.provide(requireNotNull(activityResultRegistry))
                        }

                        is AchAdditionalInfo.DisplayMandate -> {
                            trySend(
                                PrimerHeadlessEvent.DisplayMandateStripeAchMandate(
                                    onAcceptMandate = additionalInfo.onAcceptMandate,
                                    onDeclineMandate = additionalInfo.onDeclineMandate
                                )
                            )
                        }
                    }
                }
            }
        )

        awaitClose {
            headlessUniversalCheckout.cleanup()
        }
    }

    override fun start(clientToken: String, activityResultRegistry: ActivityResultRegistry) {
        PrimerLogging.logger.logLevel = if (BuildConfig.DEBUG) PrimerLogLevel.DEBUG else PrimerLogLevel.NONE
        headlessUniversalCheckout.start(
            context = context,
            clientToken = clientToken,
            settings = PrimerSettings(
                paymentMethodOptions = PrimerPaymentMethodOptions(
                    // Replace with your own Stripe publishable key
                    stripeOptions = PrimerStripeOptions(
                        mandateData = null,
                        publishableKey = BuildConfig.STRIPE_PUBLISHABLE_KEY
                    )
                )
            )
        )
        this.activityResultRegistry = activityResultRegistry
    }

    companion object {

        const val STRIPE_ACH_PAYMENT_METHOD_TYPE = "STRIPE_ACH"
    }
}
