package com.example.UserManagementService.user.login.service.JWT;


import com.example.UserManagementService.user.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service


public class JwtGeneratorService implements JwtGeneratorServiceInterface{

    private String secretKey;
    private Integer expiration;

    public JwtGeneratorService(@Value("${aggressive.secret.key}") String  secretKey, @Value("${jwt.expired}")Integer expiration) {
        this.secretKey = secretKey;
        this.expiration =expiration;
    }

    @Override
    public String generateToken(User opsUser) {
        return buildToken(buildTokenClaims(opsUser), String.valueOf(opsUser.getId()));
    }

    private String buildToken(Map<String, Object> extraClaims, String id) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(id)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(getExpirationDate(expiration))
                .signWith(getSignInKey(secretKey), SignatureAlgorithm.HS256)
                .compact();
    }


    private Key getSignInKey(String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Date getExpirationDate(int expirationInMinutes) {

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(new Date());

        calendar.add(Calendar.MINUTE, expirationInMinutes);

        return calendar.getTime();
    }

    private Map<String, Object> buildTokenClaims(User opsUser) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", opsUser.getRole());
        claims.put("firstName", opsUser.getFirstName());
        claims.put("lastName", opsUser.getLastName());
//        claims.put("id" , opsUser.getId());
        claims.put("username" , opsUser.getUsername());
        return claims;
    }

}