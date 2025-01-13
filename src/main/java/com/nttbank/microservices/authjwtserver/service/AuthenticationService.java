package com.nttbank.microservices.authjwtserver.service;


import com.nttbank.microservices.authjwtserver.config.JwtService;
import com.nttbank.microservices.authjwtserver.exception.UsernameAlreadyExistsException;
import com.nttbank.microservices.authjwtserver.model.entity.UserEntity;
import com.nttbank.microservices.authjwtserver.model.request.AuthenticationRequest;
import com.nttbank.microservices.authjwtserver.model.request.RegisterRequest;
import com.nttbank.microservices.authjwtserver.model.response.TokenResponse;
import com.nttbank.microservices.authjwtserver.model.response.UserCreatedResponse;
import com.nttbank.microservices.authjwtserver.repository.IUserRepo;
import com.nttbank.microservices.authjwtserver.util.KafkaUtil;
import com.nttbank.microservices.commonlibrary.event.CreateBankAccountEvent;
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
  private final KafkaUtil kafkaUtil;


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
      throw new UsernameAlreadyExistsException("Username already exists");
    }

    UserEntity userEntity = UserEntity.builder()
        .username(request.username())
        .roles(new String[]{"ROLE_USER"})
        .password(passwordEncoder.encode(request.password()))
        .status("active")
        .build();

    userRepository.save(userEntity);
    kafkaUtil.sendMessage(CreateBankAccountEvent.builder()
        .username(request.username())
        .email(request.email())
        .phoneNumber(request.phoneNumber())
        .imei(request.imei())
        .build());

    return UserCreatedResponse.builder()
        .status("success")
        .message("User created successfully, your wallet will be configured in some minutes.")
        .build();

  }


}
