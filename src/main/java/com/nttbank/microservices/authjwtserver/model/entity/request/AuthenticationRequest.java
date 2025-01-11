package com.nttbank.microservices.authjwtserver.model.request;

public record AuthenticationRequest(String username, String password) {
}
