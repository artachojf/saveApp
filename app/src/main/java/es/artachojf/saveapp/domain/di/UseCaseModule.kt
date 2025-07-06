package es.artachojf.saveapp.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.artachojf.saveapp.domain.movement.AddMovement
import es.artachojf.saveapp.domain.movement.GetMovement
import es.artachojf.saveapp.domain.movement.GetMovements
import es.artachojf.saveapp.domain.movement.MovementRepository
import es.artachojf.saveapp.domain.movement.MovementUseCases
import es.artachojf.saveapp.domain.movement.UpdateMovement

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideMovementUseCases(
        movementRepository: MovementRepository
    ) = MovementUseCases(
        getMovements = GetMovements(movementRepository),
        getMovement = GetMovement(movementRepository),
        addMovement = AddMovement(movementRepository),
        updateMovement = UpdateMovement(movementRepository)
    )
}