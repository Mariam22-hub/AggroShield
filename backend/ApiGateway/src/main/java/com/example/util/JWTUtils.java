package com.example.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

public class JWTUtils {

    public static boolean isTokenValid(String token, String key) {
        return !isTokenExpired(token, key);
    }

    public static String extractSubClaim(String token, String key) {

        return extractAllClaims(token, key).getSubject();
    }

    private static boolean isTokenExpired(String token, String key) {
        return extractExpiration(token, key).before(new Date());
    }

    private static Date extractExpiration(String token, String key) {
        return extractClaim(token, key,Claims::getExpiration);
    }

    public static <T> T extractClaim(String token, String key, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token, key);
        return claimsResolver.apply(claims);
    }

    public static String extractField(String token,String key, String field) {
        final Claims claims = extractAllClaims(token, key);
        return claims.get(field, String.class);
    }

    public static Claims extractAllClaims(String token, String key) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey(key))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private static Key getSignInKey(String key) {
        byte[] keyBytes = Decoders.BASE64.decode(key);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
