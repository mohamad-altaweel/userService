package com.example.userService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.*;

import com.example.userService.service.*;
import com.example.userService.security.*;

@SuppressWarnings("preview")
@Configuration
public class SecurityConfig {

    private final UserServiceImp userDetailsService;
    private final JwtTokenFilter jwtAuthFilter;
  
    public SecurityConfig(UserServiceImp userDetailsService, JwtTokenFilter jwtAuthFilter) {
      this.userDetailsService = userDetailsService;
      this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

      @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
    return http
        .cors(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
          //        Set permissions on endpoints
        .authorizeHttpRequests(auth -> auth
          //  our public endpoints
            .requestMatchers(HttpMethod.POST, "/api/auth/signup/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/auth/login/**").permitAll()
            .requestMatchers(HttpMethod.OPTIONS, "/api/auth/login/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/auth/verifyEmail/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/auth/sendVerificationEmail/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/auth/requestpassword/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/auth/reset-password/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/auth/loginAdmin/**").permitAll()
            .requestMatchers(HttpMethod.OPTIONS, "/api/auth/loginAdmin/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/authentication-docs/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/users/welcome").permitAll()
            // endpoints required admin rights
            .requestMatchers(HttpMethod.POST, "/api/users/**").hasRole("ADMIN")
          // our private endpoints
            .anyRequest().authenticated()
            )
        .authenticationManager(authenticationManager)
//        We need jwt filter before the UsernamePasswordAuthenticationFilter.
//        Since we need every request to be authenticated before going through spring security filter.
//        (UsernamePasswordAuthenticationFilter creates a UsernamePasswordAuthenticationToken from a username and password that are submitted in the HttpServletRequest.)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
    authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    return authenticationManagerBuilder.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
      CorsConfiguration configuration = new CorsConfiguration();
      configuration.setAllowedOriginPatterns(Arrays.asList("http://localhost:3000"));
      configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
      configuration.setExposedHeaders(Arrays.asList("Authorization", "content-type"));
      configuration.setAllowedHeaders(Arrays.asList("Authorization", "content-type"));
      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      source.registerCorsConfiguration("/**", configuration);
      return source;
  }
}
