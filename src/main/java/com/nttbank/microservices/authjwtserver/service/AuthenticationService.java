package com.nttbank.microservices.authjwtserver.service;


import com.nttbank.microservices.authjwtserver.config.JwtService;
import com.nttbank.microservices.authjwtserver.model.entity.UserEntity;
import com.nttbank.microservices.authjwtserver.model.request.AuthenticationRequest;
import com.nttbank.microservices.authjwtserver.model.request.RegisterRequest;
import com.nttbank.microservices.authjwtserver.model.response.TokenResponse;
import com.nttbank.microservices.authjwtserver.model.response.UserCreatedResponse;
import com.nttbank.microservices.authjwtserver.repository.IUserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final PasswordEncoder passwordEncoder;
  private final IUserRepo userRepository;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;


  public TokenResponse authenticate(AuthenticationRequest request) {

    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.username(), request.password()));

    UserEntity userEntity = userRepository.findByUsername(request.username())
        .orElseThrow(() -> new UsernameNotFoundException("User does not exist"));

    return TokenResponse.builder()
        .token(jwtService.generateToken(userEntity))
        .build();
  }


  public UserCreatedResponse register(RegisterRequest request) {

    if (userRepository.findByUsername(request.username()).isPresent()) {
      throw new IllegalArgumentException("Username already exists");
    }

    UserEntity userEntity = UserEntity.builder()
        .username(request.username())
        .name(request.name())
        .lastname(request.lastname())
        .email(request.email())
        .roles(request.roles())
        .password(passwordEncoder.encode(request.password()))
        .build();

    userRepository.save(userEntity);
    return UserCreatedResponse.builder().build();

  }


}
