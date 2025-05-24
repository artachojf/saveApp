package es.artachojf.saveapp.ui.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(ViewModelComponent::class)
object CoroutinesModule {

    @Provides
    @DispatcherIO
    fun provideDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }
}