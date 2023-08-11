package com.atmosware.musicapp.business.token;

import com.atmosware.musicapp.entity.Token;
import com.atmosware.musicapp.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class TokenManager implements TokenService {
    private final TokenRepository repository;
    public final static String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    @Override
    public void add(String token) {
        Token addToken=new Token();
        addToken.setId(null);
        addToken.setToken(token);
        addToken.setActivate(true);
        repository.save(addToken);
    }

    @Override
    public void delete(String token) {
        Token token1=repository.findByToken(token);
        repository.deleteById(token1.getId());
    }
    @Override
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        checkIfDeleteTokenExpired(token);
        final String userName = extractUserName(token);
        Token token1=repository.findByToken(token);
        return (userName.equals(userDetails.getUsername()) && token1.isActivate());
    }

    @Override
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        extraClaims.put("roles", userDetails.getAuthorities());
        return Jwts.builder().addClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 100 * 1000))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
@Override
    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private void checkIfDeleteTokenExpired(String token){
        if(isTokenExpired(token)){
            Token deleteToken=repository.findByToken(token);
            repository.deleteById(deleteToken.getId());
        }

    }
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token).getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}