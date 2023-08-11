package com.atmosware.musicapp.business.token;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public interface TokenService {
     void add(String token);
     void delete(String token);
     String extractUserName(String token);
     boolean isTokenValid(String token,UserDetails userDetails);
     String generateToken(Map<String,Object> extraClaims, UserDetails userDetails);
     <T> T extractClaim(String token, Function<Claims,T> claimResolver);

}