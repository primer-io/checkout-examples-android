package io.primer.checkout.cobadged.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import io.primer.checkout.cobadged.checkout.data.repository.PrimerCardInputRepository
import io.primer.checkout.cobadged.checkout.data.repository.DefaultPrimerHeadlessRepository
import io.primer.checkout.cobadged.checkout.data.repository.CardInputRepository
import io.primer.checkout.cobadged.checkout.data.repository.PrimerHeadlessRepository

@Module
@InstallIn(ViewModelComponent::class)
class PrimerHeadlessModule {

    @Provides
    fun providePrimerHeadlessRepository(
        @ApplicationContext context: Context
    ): PrimerHeadlessRepository {
        return DefaultPrimerHeadlessRepository(context)
    }

    @Provides
    fun provideCardInputRepository(
        @ApplicationContext context: Context
    ): CardInputRepository {
        return PrimerCardInputRepository(context)
    }
}
