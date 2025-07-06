package es.artachojf.saveapp.ui.movement.form.model

sealed interface MovementFormUIEvent {
    data object Idle : MovementFormUIEvent
    data object OnGetCategoriesFailure : MovementFormUIEvent
    data object OnGetFailure : MovementFormUIEvent
    data object OnSaveSuccess : MovementFormUIEvent
    data object OnSaveFailure : MovementFormUIEvent
    data object OnUpdateSuccess : MovementFormUIEvent
    data object OnUpdateFailure : MovementFormUIEvent
}