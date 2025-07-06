package es.artachojf.saveapp.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.artachojf.saveapp.data.category.CategoryRepositoryImpl
import es.artachojf.saveapp.data.category.CategorySupabaseDataSource
import es.artachojf.saveapp.data.login.LoginMemoryDataSource
import es.artachojf.saveapp.data.login.LoginSupabaseDataSource
import es.artachojf.saveapp.data.login.login.LoginRepositoryImpl
import es.artachojf.saveapp.data.login.logout.LogoutRepositoryImpl
import es.artachojf.saveapp.data.login.user.LoggedUserRepositoryImpl
import es.artachojf.saveapp.data.movement.MovementRepositoryImpl
import es.artachojf.saveapp.data.movement.MovementSupabaseDataSource
import es.artachojf.saveapp.domain.category.CategoryRepository
import es.artachojf.saveapp.domain.login.login.LoginRepository
import es.artachojf.saveapp.domain.login.logout.LogoutRepository
import es.artachojf.saveapp.domain.login.user.LoggedUserRepository
import es.artachojf.saveapp.domain.movement.MovementRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideLoginRepository(
        dataSource: LoginSupabaseDataSource,
        memoryDataSource: LoginMemoryDataSource
    ): LoginRepository {
        return LoginRepositoryImpl(dataSource,)
    }

    @Provides
    @Singleton
    fun provideLoggedUserRepository(
        dataSource: LoginSupabaseDataSource,
        memoryDataSource: LoginMemoryDataSource
    ): LoggedUserRepository {
        return LoggedUserRepositoryImpl(dataSource, memoryDataSource)
    }

    @Provides
    @Singleton
    fun provideLogoutRepository(
        dataSource: LoginSupabaseDataSource,
        memoryDataSource: LoginMemoryDataSource
    ): LogoutRepository {
        return LogoutRepositoryImpl(dataSource, memoryDataSource)
    }

    @Provides
    @Singleton
    fun provideMovementRepository(
        dataSource: MovementSupabaseDataSource
    ): MovementRepository {
        return MovementRepositoryImpl(dataSource)
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(
        dataSource: CategorySupabaseDataSource
    ): CategoryRepository {
        return CategoryRepositoryImpl(dataSource)
    }
}