package io.primer.checkout.stripe.ach.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import io.primer.checkout.stripe.ach.checkout.data.repository.DefaultPrimerHeadlessRepository
import io.primer.checkout.stripe.ach.checkout.data.repository.PrimerHeadlessRepository

@Module
@InstallIn(ViewModelComponent::class)
class PrimerHeadlessModule {
    @Provides
    fun providePrimerHeadlessRepository(
        @ApplicationContext appContext: Context
    ): PrimerHeadlessRepository = DefaultPrimerHeadlessRepository(
        context = appContext
    )
}
