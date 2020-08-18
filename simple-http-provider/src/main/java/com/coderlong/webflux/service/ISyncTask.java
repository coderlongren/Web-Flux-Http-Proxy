package com.coderlong.webflux.service;

import java.util.concurrent.CompletableFuture;

public interface ISyncTask<T, R> {
    CompletableFuture<R> execute(T request);
}
