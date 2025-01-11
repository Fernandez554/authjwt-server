package com.nttbank.microservices.authjwtserver.config;

import com.nttbank.microservices.authjwtserver.model.entity.UserEntity;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {


    @Value("${nttbank.security.key:jfernandez}")
    private String securityKey;


    public String generateToken(UserDetails userDetails) {

        UserEntity userEntity = (UserEntity) userDetails;


        Map<String, Object> customClaims = new HashMap<>();
        customClaims.put("username", userEntity.getUsername());
        customClaims.put("email", userEntity.getEmail());
        customClaims.put("lastname", userEntity.getLastname());
        customClaims.put("authorities", userEntity.getRoles());


        return Jwts.builder()
                .setSubject(userEntity.getUsername())
                .setIssuedAt(new Date())
                .signWith(getSignKey())
                .setExpiration(new Date(System.currentTimeMillis() + (300 * 1000)))
                .addClaims(customClaims)
                .compact();
    }

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(Base64.getEncoder().encode(securityKey.getBytes()));
    }

}
