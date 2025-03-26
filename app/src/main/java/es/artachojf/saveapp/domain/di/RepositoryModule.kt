package es.artachojf.saveapp.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.artachojf.saveapp.data.login.anonymous.AnonymousLoginRepositoryImpl
import es.artachojf.saveapp.data.login.google.GoogleLoginRepositoryImpl
import es.artachojf.saveapp.data.login.LoginSupabaseDataSource
import es.artachojf.saveapp.data.login.logout.LogoutRepositoryImpl
import es.artachojf.saveapp.data.login.user.LoggedUserRepositoryImpl
import es.artachojf.saveapp.domain.login.anonymous.AnonymousLoginRepository
import es.artachojf.saveapp.domain.login.google.GoogleLoginRepository
import es.artachojf.saveapp.domain.login.logout.LogoutRepository
import es.artachojf.saveapp.domain.login.user.LoggedUserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideGoogleLoginRepository(
        dataSource: LoginSupabaseDataSource
    ): GoogleLoginRepository {
        return GoogleLoginRepositoryImpl(dataSource)
    }

    @Provides
    @Singleton
    fun provideAnonymousLoginRepository(
        dataSource: LoginSupabaseDataSource
    ): AnonymousLoginRepository {
        return AnonymousLoginRepositoryImpl(dataSource)
    }

    @Provides
    @Singleton
    fun provideLoggedUserRepository(
        dataSource: LoginSupabaseDataSource
    ): LoggedUserRepository {
        return LoggedUserRepositoryImpl(dataSource)
    }

    @Provides
    @Singleton
    fun provideLogoutRepository(
        dataSource: LoginSupabaseDataSource
    ): LogoutRepository {
        return LogoutRepositoryImpl(dataSource)
    }
}