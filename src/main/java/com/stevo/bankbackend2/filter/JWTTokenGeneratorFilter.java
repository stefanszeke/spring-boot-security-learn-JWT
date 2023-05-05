package com.stevo.bankbackend2.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Date;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.stevo.bankbackend2.constants.SecurityConstants;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTTokenGeneratorFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

        System.out.println("**** JWTTokenGeneratorFilter ****");
        System.out.println("**** JWTTokenGeneratorFilter ****");

    // get the authenticationobject from the security context
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (null != authentication) {
      SecretKey key = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));
      String jwt = Jwts.builder()
          .setIssuer("Bankbackend2")
          .setSubject(authentication.getName())
          .claim("authorities", extractAuthorities(authentication))
          .setIssuedAt(new Date())
          .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(5)))
          .signWith(key)
          .compact();

      response.addHeader("Authorization", "Bearer " + jwt);
    }

    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    return !request.getServletPath().equals("/user");
  }

  private String extractAuthorities(Authentication authentication) {
    return authentication.getAuthorities().stream().map(authority -> authority.getAuthority())
        .collect(Collectors.joining(", "));
  }

}
