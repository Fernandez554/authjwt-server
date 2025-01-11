package com.nttbank.microservices.authjwtserver.model.request;

public record RegisterRequest(String id,
                              String name,
                              String lastname,
                              String username,
                              String email,
                              String password,
                              String[] roles) {
}
