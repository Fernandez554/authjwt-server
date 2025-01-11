package com.nttbnak.microservices.authjwtserver.service;


import com.nttbnak.microservices.authjwtserver.config.JwtService;
import com.nttbnak.microservices.authjwtserver.model.entity.UserEntity;
import com.nttbnak.microservices.authjwtserver.model.request.AuthenticationRequest;
import com.nttbnak.microservices.authjwtserver.model.request.RegisterRequest;
import com.nttbnak.microservices.authjwtserver.service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public String authenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        UserEntity userEntity = userRepository.findByUsername(request.username()).orElse(null);

        if(userEntity == null){
            throw new UsernameNotFoundException("Usuario no registrado en BD");
        }

        return jwtService.generateToken(userEntity);

    }


    public String register(RegisterRequest request) {

        UserEntity userEntity = UserEntity.builder()
                .username(request.username())
                .name(request.name())
                .lastname(request.lastname())
                .email(request.email())
                .roles(request.roles())
                .password(passwordEncoder.encode(request.password()))
                .build();

        userRepository.save(userEntity);


        return jwtService.generateToken(userEntity);

    }


}
