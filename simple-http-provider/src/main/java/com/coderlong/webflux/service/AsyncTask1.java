package com.coderlong.webflux.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

@Service
public class AsyncTask1 implements ISyncTask<String, Object>{


    @Override
    public CompletableFuture<Object> execute(String request) {
        return CompletableFuture.supplyAsync(() -> {
            return new Object();
        });
    }
}
