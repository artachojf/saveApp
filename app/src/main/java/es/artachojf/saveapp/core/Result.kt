package es.artachojf.saveapp.core

sealed class Result<out S, out F> {
    data class Success<out S>(val data: S) : Result<S, Nothing>()
    data class Failure<out F>(val error: F) : Result<Nothing, F>()
}

fun <S, F, S2, F2> Result<S, F>.map(
    mapSuccess: (S) -> S2,
    mapFailure: (F) -> F2
): Result<S2, F2> {
    return when (this) {
        is Result.Success -> Result.Success(mapSuccess(data))

        is Result.Failure -> Result.Failure(mapFailure(error))
    }
}