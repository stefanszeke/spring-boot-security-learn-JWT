package com.stevo.bankbackend2.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

import com.stevo.bankbackend2.constants.SecurityConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTTokenValidatorFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String jwt = request.getHeader("Authorization");

    System.out.println("**** JWTTokenValidatorFilter ****");
    System.out.println("**** JWTTokenValidatorFilter ****");
    System.out.println("**** JWTTokenValidatorFilter ****");

    if (null != jwt) {
      try {
        jwt = jwt.replace("Bearer ", "");

        System.out.println("jwt: " + jwt);

        Claims claims = validateToken(jwt);

        String username = claims.getSubject();
        String authorities = (String) claims.get("authorities");

        Authentication authentication = new UsernamePasswordAuthenticationToken(
            username,
            null,
            AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));

        SecurityContextHolder.getContext().setAuthentication(authentication);

      } catch (Exception e) {
        throw new BadCredentialsException("Invalid JWT" + e.getMessage());
      }
    }
    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String[] pathsToSkip = {"/user", "/register"};
    return Arrays.stream(pathsToSkip).anyMatch(path -> request.getServletPath().equals(path));
  }
  

  private Claims validateToken(String jwt) {
    SecretKey key = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));

    Jws<Claims> claimsJws = Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(jwt);

    return claimsJws.getBody();
  }
}
