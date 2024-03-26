package io.primer.checkout.dropin.checkout.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import io.primer.android.Primer
import io.primer.android.PrimerCheckoutListener
import io.primer.android.completion.PrimerErrorDecisionHandler
import io.primer.android.core.logging.PrimerLogLevel
import io.primer.android.core.logging.PrimerLogging
import io.primer.android.domain.PrimerCheckoutData
import io.primer.android.domain.error.models.PrimerError
import io.primer.checkout.dropin.BuildConfig
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

interface PrimerDropInRepository {

    fun start(clientToken: String)

    val primerDropInEvents: Flow<PrimerDropInEvent>
}

sealed class PrimerDropInEvent {
    data class CheckoutFailed(
        val errorMessage: String,
        val checkoutData: PrimerCheckoutData?,
        val errorHandler: PrimerErrorDecisionHandler?
    ) : PrimerDropInEvent()

    data class CheckoutCompleted(val checkoutData: PrimerCheckoutData) : PrimerDropInEvent()
}

class DefaultPrimerDropInRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val errorMapper: ErrorMapper
) : PrimerDropInRepository {

    private val dropInCheckout = Primer.instance

    override val primerDropInEvents: Flow<PrimerDropInEvent> = callbackFlow {
        dropInCheckout.configure(
            listener = object : PrimerCheckoutListener {

                override fun onCheckoutCompleted(checkoutData: PrimerCheckoutData) {
                    trySend(PrimerDropInEvent.CheckoutCompleted(checkoutData))
                }

                override fun onFailed(
                    error: PrimerError,
                    checkoutData: PrimerCheckoutData?,
                    errorHandler: PrimerErrorDecisionHandler?
                ) {
                    errorHandler?.showErrorMessage(errorMapper.toErrorMessage(error, checkoutData))
                    trySend(
                        PrimerDropInEvent.CheckoutFailed(
                            error.description,
                            checkoutData,
                            errorHandler
                        )
                    )
                }
            }
        )

        awaitClose {
            dropInCheckout.cleanup()
        }
    }

    override fun start(clientToken: String) {
        PrimerLogging.logger.logLevel =
            if (BuildConfig.DEBUG) PrimerLogLevel.DEBUG else PrimerLogLevel.NONE
        dropInCheckout.showUniversalCheckout(context, clientToken)
    }
}
