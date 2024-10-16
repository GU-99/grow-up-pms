package com.growup.pms.common.security.jwt;

import com.growup.pms.auth.controller.dto.SecurityUser;
import com.growup.pms.common.security.jwt.dto.TokenResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
    private static final String USER_ID_KEY = "id";

    private SecretKey key;
    private final String base64Secret;
    public final long refreshTokenExpirationMillis;
    public final long accessTokenExpirationMillis;

    public JwtTokenProvider(
            @Value("${security.jwt.base64-secret}") String base64Secret,
            @Value("${security.jwt.refresh-token-expiration-millis}") long refreshTokenExpirationMillis,
            @Value("${security.jwt.access-token-expiration-millis}") long accessTokenExpirationMillis
    ) {
        this.base64Secret = base64Secret;
        this.refreshTokenExpirationMillis = refreshTokenExpirationMillis;
        this.accessTokenExpirationMillis = accessTokenExpirationMillis;
    }

    public String createToken(SecurityUser user, long expirationTime) {
        String authorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        return Jwts.builder()
                .subject(user.getUsername())
                .claim(USER_ID_KEY, user.getId())
                .claim(AUTHORITIES_KEY, authorities)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key, Jwts.SIG.HS512)
                .compact();
    }

    public TokenResponse generateToken(SecurityUser user) {
        return TokenResponse.builder()
                .accessToken(createToken(user, accessTokenExpirationMillis))
                .refreshToken(createToken(user, refreshTokenExpirationMillis))
                .build();
    }

    private Jws<Claims> getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
    }

    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token)
                .getPayload()
                .getSubject();
    }

    public Long getUserIdFromToken(String token) {
        return getAllClaimsFromToken(token)
                .getPayload()
                .get(USER_ID_KEY, Long.class);
    }

    @PostConstruct
    public void init() {
        byte[] secretKeyBytes = Decoders.BASE64.decode(base64Secret);
        this.key = Keys.hmacShaKeyFor(secretKeyBytes);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getAllClaimsFromToken(token).getPayload();
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(Optional.ofNullable(claims.get(AUTHORITIES_KEY))
                                .map(Object::toString)
                                .orElse("")
                                .split(","))
                        .map(String::trim)
                        .filter(auth -> !auth.isEmpty())
                        .map(SimpleGrantedAuthority::new)
                        .toList();
        SecurityUser principal = new SecurityUser(claims.get(USER_ID_KEY, Long.class), claims.getSubject(), "");
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String token) {
        try {
            getAllClaimsFromToken(token);
            return true;
        } catch (JwtException ex) {
            log.trace("Invalid JWT token trace: {}", ex.toString());
            return false;
        }
    }
}
