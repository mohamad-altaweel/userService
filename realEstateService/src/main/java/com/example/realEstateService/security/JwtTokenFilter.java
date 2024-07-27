package com.example.realEstateService.security;

import com.example.realEstateService.controller.dto.ApiErrorResponse;
import com.example.realEstateService.exception.AccessDeniedException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

  private ObjectMapper objectMapper;

  public JwtTokenFilter(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      String authHeader = request.getHeader("Authorization");

      String token = null;
      String username = null;
      if (authHeader != null && authHeader.startsWith("Bearer ")) {
        token = authHeader.substring(7);
        username = JwtHelper.extractUsername(token);
      }

      //If the accessToken is null. It will pass the request to next filter in the chain.
      //Any login and signup requests will not have jwt token in their header, therefore they will be passed to next filter chain.
      if (token == null) {
        filterChain.doFilter(request, response);
        return;
      }

      //If any accessToken is present, then it will validate the token and then authenticate the request in security context
      if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        if (JwtHelper.isTokenExpired(token)) {
          UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, null);
          authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
      }

      filterChain.doFilter(request, response);
    } catch (AccessDeniedException e) {
      ApiErrorResponse errorResponse = new ApiErrorResponse(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      response.getWriter().write(toJson(errorResponse));
    }
  }

  private String toJson(ApiErrorResponse response) {
    try {
      return objectMapper.writeValueAsString(response);
    } catch (Exception e) {
      return ""; // Return an empty string if serialization fails
    }
  }
}