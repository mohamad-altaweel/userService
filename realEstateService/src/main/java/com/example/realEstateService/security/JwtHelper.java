package com.example.realEstateService.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;

import java.util.Date;

import java.util.function.Function;


import com.example.realEstateService.exception.AccessDeniedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JwtHelper {

  private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
  private static final Logger logger = LoggerFactory.getLogger(JwtHelper.class);


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

  public static boolean isTokenExpired(String token) {
    Claims claims = getTokenBody(token);
    boolean isTrustedIssuer = claims.getIssuer().equals("Mohamad");
    return claims.getExpiration().before(new Date()) || (!isTrustedIssuer);
  }
}
