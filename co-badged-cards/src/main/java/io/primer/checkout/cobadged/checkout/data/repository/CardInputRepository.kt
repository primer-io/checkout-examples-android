package io.primer.checkout.cobadged.checkout.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import io.primer.android.components.domain.core.models.card.PrimerCardMetadataState
import io.primer.android.components.domain.core.models.metadata.PrimerPaymentMethodMetadataState
import io.primer.android.components.domain.error.PrimerInputValidationError
import io.primer.android.components.domain.inputs.models.PrimerInputElementType
import io.primer.android.components.manager.raw.PrimerHeadlessUniversalCheckoutRawDataManager
import io.primer.android.components.manager.raw.PrimerHeadlessUniversalCheckoutRawDataManagerInterface
import io.primer.android.components.manager.raw.PrimerHeadlessUniversalCheckoutRawDataManagerListener
import io.primer.android.components.ui.assets.PrimerHeadlessUniversalCheckoutAssetsManager
import io.primer.checkout.cobadged.checkout.data.extension.findErrorById
import io.primer.checkout.cobadged.checkout.data.extension.toPrimerCardData
import io.primer.checkout.cobadged.checkout.data.model.CardInput
import io.primer.checkout.cobadged.checkout.data.model.CardNetworkMetadata
import io.primer.checkout.cobadged.checkout.data.model.CardNetworksState
import io.primer.checkout.cobadged.checkout.data.model.CardValidation
import io.primer.checkout.cobadged.checkout.data.model.ValidationErrors
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull

interface CardInputRepository {

    val validationEvents: Flow<CardValidation>

    val cardNetworksState: Flow<CardNetworksState>

    fun isCardholderNameEnabled(): Boolean

    fun updateCardData(cardInput: CardInput)

    fun submit()

    fun getAllowedCardsNetworks(): List<CardNetworkMetadata>
}

class PrimerCardInputRepository(@ApplicationContext private val context: Context) :
    CardInputRepository,
    PrimerHeadlessUniversalCheckoutRawDataManagerListener {

    private val rawDataManager: PrimerHeadlessUniversalCheckoutRawDataManagerInterface by lazy {
        PrimerHeadlessUniversalCheckoutRawDataManager.newInstance(
            DefaultPrimerHeadlessRepository.CARD_PAYMENT_METHOD_TYPE
        ).also { manager ->
            manager.setListener(this)
        }
    }

    private val _validationEvents: MutableStateFlow<CardValidation?> = MutableStateFlow(null)
    override val validationEvents: Flow<CardValidation> =
        _validationEvents.filterNotNull()

    private val _cardNetworksState: MutableStateFlow<CardNetworksState?> =
        MutableStateFlow(null)
    override val cardNetworksState: Flow<CardNetworksState> = _cardNetworksState.filterNotNull()

    override fun isCardholderNameEnabled(): Boolean {
        return rawDataManager.getRequiredInputElementTypes()
            .contains(PrimerInputElementType.CARDHOLDER_NAME)
    }

    override fun updateCardData(cardInput: CardInput) {
        rawDataManager.setRawData(cardInput.toPrimerCardData())
    }

    override fun submit() {
        rawDataManager.submit()
    }

    override fun getAllowedCardsNetworks() =
        PrimerHeadlessUniversalCheckoutAssetsManager.getAllowedCardsNetworkAssets(context)
            .values.map { asset ->
                CardNetworkMetadata(
                    asset.cardNetwork,
                    asset.cardNetwork.name,
                    asset.cardNetworkIcon.colored
                )
            }

    override fun onValidationChanged(
        isValid: Boolean,
        errors: List<PrimerInputValidationError>
    ) {
        _validationEvents.tryEmit(
            CardValidation(
                isValid,
                ValidationErrors(
                    errors.findErrorById(CARD_NUMBER_ERROR_ID) ?: errors.findErrorById(
                        CARD_TYPE_ERROR_ID
                    ),
                    errors.findErrorById(CARD_EXPIRY_DATE_ERROR_ID),
                    errors.findErrorById(CARD_CVV_ERROR_ID),
                    errors.findErrorById(CARDHOLDER_NAME_ERROR_ID)
                )
            )
        )
    }

    override fun onMetadataStateChanged(metadataState: PrimerPaymentMethodMetadataState) {
        super.onMetadataStateChanged(metadataState)
        when (metadataState) {
            is PrimerCardMetadataState -> {
                when (metadataState) {
                    is PrimerCardMetadataState.Fetched ->
                        _cardNetworksState.tryEmit(
                            metadataState.cardMetadata.let { metadata ->
                                CardNetworksState.CardNetworksChanged(
                                    metadata.canSelectCardNetwork,
                                    metadata.allowedDetectedCardNetworks.map { primerCardNetwork ->
                                        CardNetworkMetadata(
                                            primerCardNetwork.network,
                                            primerCardNetwork.displayName,
                                            PrimerHeadlessUniversalCheckoutAssetsManager
                                                .getCardNetworkAssets(
                                                    context,
                                                    primerCardNetwork.network
                                                ).cardNetworkIcon.colored
                                        )
                                    },
                                    metadata.preferredCardNetwork?.let { primerCardNetwork ->
                                        CardNetworkMetadata(
                                            primerCardNetwork.network,
                                            primerCardNetwork.displayName,
                                            PrimerHeadlessUniversalCheckoutAssetsManager
                                                .getCardNetworkAssets(
                                                    context,
                                                    primerCardNetwork.network
                                                ).cardNetworkIcon.colored
                                        )
                                    }
                                )
                            }
                        )

                    is PrimerCardMetadataState.Fetching -> _cardNetworksState.tryEmit(
                        CardNetworksState.CardNetworksLoading
                    )
                }
            }

            else -> Unit
        }
    }

    private companion object {

        const val CARD_NUMBER_ERROR_ID = "invalid-card-number"
        const val CARD_EXPIRY_DATE_ERROR_ID = "invalid-expiry-date"
        const val CARD_CVV_ERROR_ID = "invalid-cvv"
        const val CARDHOLDER_NAME_ERROR_ID = "invalid-cardholder-name"
        const val CARD_TYPE_ERROR_ID = "invalid-card-type"
    }
}
