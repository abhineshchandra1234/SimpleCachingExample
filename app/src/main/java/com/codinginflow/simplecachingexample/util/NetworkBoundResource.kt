package com.codinginflow.simplecachingexample.util

import android.app.DownloadManager
import kotlinx.coroutines.flow.flow
import java.util.concurrent.Flow

fun <ResultType, RequestType> networkBoundResource(
    query: () -> Flow<ResultType>,
    fetch: suspend () -> RequestType,
    saveFetchResult: suspend (RequestType) -> Unit,
    shouldFetch: (ResultType) -> Boolean = { true }
) = flow {
    val data = query().first()

    if (shouldFetch(data)) {
        emit(Resource.Loading(data))
        try {
            saveFetchResult(fetch())
            query().map { Resource.Success(it)}
        } catch (throwable: Throwable) {

        }

    }
}