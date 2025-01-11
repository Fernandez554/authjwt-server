package com.nttbank.microservices.authjwtserver.expose.web;

import com.nttbank.microservices.authjwtserver.model.request.AuthenticationRequest;
import com.nttbank.microservices.authjwtserver.model.request.RegisterRequest;
import com.nttbank.microservices.authjwtserver.model.response.TokenResponse;
import com.nttbank.microservices.authjwtserver.model.response.UserCreatedResponse;
import com.nttbank.microservices.authjwtserver.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {


  private final AuthenticationService authenticationService;

  @PostMapping("/register")
  public ResponseEntity<UserCreatedResponse> register(@RequestBody RegisterRequest request) {
    return ResponseEntity.ok(authenticationService.register(request));
  }

  @PostMapping("/login")
  public ResponseEntity<TokenResponse> login(@Valid @RequestBody AuthenticationRequest request) {
    return ResponseEntity.ok(authenticationService.authenticate(request));
  }


}
