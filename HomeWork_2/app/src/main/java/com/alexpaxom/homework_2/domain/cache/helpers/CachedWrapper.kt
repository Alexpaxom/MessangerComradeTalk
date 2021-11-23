package com.alexpaxom.homework_2.domain.cache.helpers

sealed class CachedWrapper<T>(val data: T) {
    class CachedData<T> (data: T): CachedWrapper<T>(data)
    class OriginalData<T> (data: T): CachedWrapper<T>(data)
    class ErrorResult<T> (data: T, val error: Throwable): CachedWrapper<T>(data)
}

