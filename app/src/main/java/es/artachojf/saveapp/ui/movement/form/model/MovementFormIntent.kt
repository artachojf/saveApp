package es.artachojf.saveapp.ui.movement.form.model

sealed interface MovementFormIntent {
    data object GoBack : MovementFormIntent
    data object OnIncomeClicked : MovementFormIntent
    data object OnExpenseClicked : MovementFormIntent
    data class OnAmountChanged(val amount: String) : MovementFormIntent
    data class OnTitleChanged(val title: String) : MovementFormIntent
    data class OnCategorySelected(val categoryId: Int?) : MovementFormIntent
    data class OnDateSelected(val date: Long?) : MovementFormIntent
    data class OnDescriptionChanged(val description: String) : MovementFormIntent
    data object OnSaveMovement : MovementFormIntent
}