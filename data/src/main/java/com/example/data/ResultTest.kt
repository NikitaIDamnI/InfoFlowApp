package com.example.data

data class ResultTest<T>(
    val data: T? = null,
    val state: State = State.Initial,
) {
    sealed class State {
        data object Initial : State()
        data object Loading : State()
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

//internal fun <T : Any> Result<T>.toResultTest(): ResultTest<T> {
//    return when {
//        isSuccess ->
//        isFailure -> ResultTest(state = ResultTest.State.Error(error = exceptionOrNull()!!))
//        else -> error("Impossible branch")
//    }
//}
