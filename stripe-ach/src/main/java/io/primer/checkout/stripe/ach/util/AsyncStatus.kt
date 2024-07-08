package io.primer.checkout.stripe.ach.util

sealed class AsyncStatus<out T> {
    data object Loading : AsyncStatus<Nothing>()

    data class Error(val throwable: Throwable) : AsyncStatus<Nothing>()

    data class Success<out T>(val data: T) : AsyncStatus<T>()
}
