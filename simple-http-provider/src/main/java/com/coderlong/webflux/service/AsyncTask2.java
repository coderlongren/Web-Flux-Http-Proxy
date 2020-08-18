package com.coderlong.webflux.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

@Service()
public class AsyncTask2 implements ISyncTask<Integer, Integer>{

    @Override
    public CompletableFuture<Integer> execute(Integer request) {
        return CompletableFuture.supplyAsync(() -> {
            return 2;
        });
    }
}
