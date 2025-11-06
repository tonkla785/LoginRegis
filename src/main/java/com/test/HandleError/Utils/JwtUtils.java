package com.test.HandleError.Utils;

import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JwtUtils {
    @Value("${jwt.secret:mySuperSecureKeyThatIsAtLeast32BytesLong}")
    private String secret;
    @Value("${jwt.expiration:86400000}")
    private Long expiration;

    private SecretKey getSigninKey() {
        byte[] keyBytes = secret.getBytes();
        return new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    public String generateToken(String email) { // Use email as username
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder().subject(email).issuedAt(now).expiration(expiryDate).signWith(getSigninKey()).compact();
    }

    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser().verifyWith(getSigninKey()).build().parseSignedClaims(token).getPayload();
        return claims.getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().verifyWith(getSigninKey()).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parser().verifyWith(getSigninKey()).build().parseSignedClaims(token).getPayload();
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
}
