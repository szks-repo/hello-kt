package domain.validation

/**
 * Represents the outcome of validating a value originating from a platform or dynamic source.
 */
sealed interface ValidationResult<out T> {
    data class Valid<T>(val value: T) : ValidationResult<T>

    data class Invalid(val errors: List<String>) : ValidationResult<Nothing> {
        constructor(message: String) : this(listOf(message))
    }
}

inline fun <T, R> ValidationResult<T>.map(transform: (T) -> R): ValidationResult<R> =
    when (this) {
        is ValidationResult.Valid -> ValidationResult.Valid(transform(value))
        is ValidationResult.Invalid -> this
    }

inline fun <T, R> ValidationResult<T>.flatMap(transform: (T) -> ValidationResult<R>): ValidationResult<R> =
    when (this) {
        is ValidationResult.Valid -> transform(value)
        is ValidationResult.Invalid -> this
    }

inline fun <T> ValidationResult<T>.onValid(block: (T) -> Unit): ValidationResult<T> {
    if (this is ValidationResult.Valid) block(value)
    return this
}

inline fun <T> ValidationResult<T>.onInvalid(block: (List<String>) -> Unit): ValidationResult<T> {
    if (this is ValidationResult.Invalid) block(errors)
    return this
}
