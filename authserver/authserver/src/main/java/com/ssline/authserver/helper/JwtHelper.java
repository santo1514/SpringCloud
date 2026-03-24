package com.ssline.authserver.helper;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtHelper {

    @Value("${application.jwt.secret}")
    private String jwtSecret;

    private final static String  EXCEPTION_MESSAGE = "Invalid JWT.";

    public String createToken(String username){
        final var now = new Date();
        var expirationDate = new Date(now.getTime() + (3600000));
        return Jwts.builder()
                    .setSubject(username)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(expirationDate)
                    .signWith(getSecretKey())
                    .compact();
    }

    public boolean validateToken(String token){
        try{
            final var expirationDate = getExpirationDateFromToken(token);
            return expirationDate.after(new Date());
        }catch(Exception ex){
             throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, EXCEPTION_MESSAGE);
        }

    }

    private Date getExpirationDateFromToken(String token){
        return getClaimsFromToken(token, Claims::getExpiration);
    }


    private <T>T getClaimsFromToken(String token, Function<Claims, T> claimsResolver){
        return claimsResolver.apply(signToken(token));
    }
    
    private Claims signToken(String jwt){
        return Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
    }

    private SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
}
