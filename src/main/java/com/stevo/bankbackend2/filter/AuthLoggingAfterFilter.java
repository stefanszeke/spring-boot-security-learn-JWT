package com.stevo.bankbackend2.filter;

import java.io.IOException;
import java.util.logging.Logger;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

public class AuthLoggingAfterFilter implements Filter {

  final Logger LOG = Logger.getLogger(AuthLoggingAfterFilter.class.getName());

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

        System.out.println("\n*** AuthLoggingAfterFilter.doFilter() ***\n");

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (null != auth) {
      LOG.info("[User: " + auth.getName() + "]| [Authorities: " + auth.getAuthorities() + "]");
    }
    chain.doFilter(request, response);
  }

}
