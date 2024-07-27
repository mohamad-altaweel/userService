package com.example.userService.security;

import com.example.userService.exception.AccessDeniedException;
import com.example.userService.model.User;
import com.example.userService.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JwtHelper {

  private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
  private static final int MINUTES = 60;
  private static final Logger logger = LoggerFactory.getLogger(JwtHelper.class);

  public static String generateToken(String email, String role) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("Roles",role);

    var now = Instant.now();
    return Jwts.builder()
        .subject(email)
        .claims(claims)
        .issuedAt(Date.from(now))
        .expiration(Date.from(now.plus(MINUTES, ChronoUnit.MINUTES)))
        .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
        .compact();
  }

  public static String extractUsername(String token) {
    return getTokenBody(token).getSubject();
  }

  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  private Claims getAllClaimsFromToken(String token) {
    return ((JwtParser) Jwts.parser().setSigningKey(SECRET_KEY)).parseClaimsJws(token).getBody();
}

  public static Boolean validateToken(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    logger.info("Username found in token " + username);
    logger.info("Username saved " + userDetails.getUsername());
    return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
  }

  private static Claims getTokenBody(String token) {
    try {
      return Jwts
          .parser()
          .setSigningKey(SECRET_KEY)
          .build()
          .parseSignedClaims(token)
          .getPayload();
    } catch (SignatureException | ExpiredJwtException e) { // Invalid signature or expired token
      throw new AccessDeniedException("Access denied: " + e.getMessage());
    }
  }

  private static boolean isTokenExpired(String token) {
    Claims claims = getTokenBody(token);
    return claims.getExpiration().before(new Date());
  }
}
