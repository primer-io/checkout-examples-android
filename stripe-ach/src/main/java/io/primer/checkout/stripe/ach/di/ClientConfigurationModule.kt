package io.primer.checkout.stripe.ach.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.primer.checkout.stripe.ach.configuration.data.ClientSessionRepository
import io.primer.checkout.stripe.ach.configuration.data.api.ClientSessionService
import io.primer.checkout.stripe.ach.configuration.data.DefaultClientSessionRepository
import io.primer.checkout.stripe.ach.configuration.validation.ClientTokenValidator
import io.primer.checkout.stripe.ach.configuration.validation.SimpleClientTokenValidator
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ClientConfigurationModule {

    @Singleton
    @Provides
    fun provideClientSessionService(): ClientSessionService {
        return ClientSessionService.create()
    }

    @Singleton
    @Provides
    fun provideClientSessionRepository(
        clientSessionService: ClientSessionService
    ): ClientSessionRepository {
        return DefaultClientSessionRepository(clientSessionService)
    }

    @Singleton
    @Provides
    fun provideClientTokenValidator(): ClientTokenValidator {
        return SimpleClientTokenValidator()
    }
}
