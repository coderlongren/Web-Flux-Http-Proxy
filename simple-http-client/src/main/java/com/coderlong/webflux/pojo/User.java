package com.coderlong.webflux.pojo;

import java.io.Serializable;

import lombok.Data;

@Data
public class User implements Serializable {
    private String name;

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                '}';
    }
}
