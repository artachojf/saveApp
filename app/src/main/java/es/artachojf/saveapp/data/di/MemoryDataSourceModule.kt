package es.artachojf.saveapp.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.artachojf.saveapp.data.login.LoginMemoryDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MemoryDataSourceModule {
    @Provides
    @Singleton
    fun provideLoginMemoryDataSource(): LoginMemoryDataSource {
        return LoginMemoryDataSource()
    }
}