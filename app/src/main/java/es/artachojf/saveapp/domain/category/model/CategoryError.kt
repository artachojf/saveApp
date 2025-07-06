package es.artachojf.saveapp.domain.category.model

sealed interface CategoryError {
    data object GenericError : CategoryError
    data object GetError : CategoryError
}