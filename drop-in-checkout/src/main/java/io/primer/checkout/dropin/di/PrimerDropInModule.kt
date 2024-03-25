package io.primer.checkout.dropin.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import io.primer.checkout.dropin.checkout.data.repository.DefaultErrorMapper
import io.primer.checkout.dropin.checkout.data.repository.DefaultPrimerDropInRepository
import io.primer.checkout.dropin.checkout.data.repository.ErrorMapper
import io.primer.checkout.dropin.checkout.data.repository.PrimerDropInRepository

@Module
@InstallIn(ViewModelComponent::class)
class PrimerDropInModule {

    @Provides
    fun providePrimerDropInRepository(
        @ApplicationContext context: Context,
        errorMapper: ErrorMapper
    ): PrimerDropInRepository {
        return DefaultPrimerDropInRepository(context, errorMapper)
    }

    @Provides
    fun provideErrorMapper(
        @ApplicationContext context: Context
    ): ErrorMapper {
        return DefaultErrorMapper(context)
    }
}
