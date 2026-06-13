package net

/**
 * Represents the outcome of a [ServerGateway] operation.
 * Each method returns either [Success] carrying the typed result value or
 * [Failure] carrying a human-readable error description.
 *
 * @param T the type of the value carried on the success path
 */
sealed class GatewayResult<out T> {

    /**
     * The operation completed successfully.
     *
     * @property value the result produced by the operation
     */
    data class Success<out T>(val value: T) : GatewayResult<T>()

    /**
     * The operation failed.
     *
     * @property message a human-readable description of the failure reason
     */
    data class Failure(val message: String) : GatewayResult<Nothing>()
}

/**
 * Transforms the value inside [GatewayResult.Success] using [transform].
 * A [GatewayResult.Failure] is returned unchanged.
 *
 * @param T the original value type
 * @param R the transformed value type
 * @param transform the mapping function applied on success
 * @return a new [GatewayResult] with the transformed value or the original failure
 */
inline fun <T, R> GatewayResult<T>.map(transform: (T) -> R): GatewayResult<R> = when (this) {
    is GatewayResult.Success -> GatewayResult.Success(transform(value))
    is GatewayResult.Failure -> this
}

/**
 * Executes [action] with the success value when this result is [GatewayResult.Success].
 * Returns this result unchanged in both cases.
 *
 * @param T the value type
 * @param action the block to execute on success
 * @return this result
 */
inline fun <T> GatewayResult<T>.onSuccess(action: (T) -> Unit): GatewayResult<T> {
    if (this is GatewayResult.Success) action(value)
    return this
}

/**
 * Executes [action] with the failure message when this result is [GatewayResult.Failure].
 * Returns this result unchanged in both cases.
 *
 * @param T the value type
 * @param action the block to execute on failure
 * @return this result
 */
inline fun <T> GatewayResult<T>.onFailure(action: (String) -> Unit): GatewayResult<T> {
    if (this is GatewayResult.Failure) action(message)
    return this
}