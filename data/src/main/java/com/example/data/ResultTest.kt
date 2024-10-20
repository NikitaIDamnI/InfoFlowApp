package com.example.data

data class ResultTest<T>(
    val data: T? = null,
    val state: State = State.Initial,
) {
    sealed class State {
        data object Initial : State()
        data object Success : State()
        data class Error(
            val error: Throwable
        ) : State()
    }
}

public fun <I : Any, O : Any> ResultTest<I>.map(mapper: (I) -> O): ResultTest<O> = ResultTest(
    data = data?.let(mapper),
    state = this.state
)
