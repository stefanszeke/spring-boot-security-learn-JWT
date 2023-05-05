package com.stevo.bankbackend2.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.stevo.bankbackend2.model.Authority;
import com.stevo.bankbackend2.model.Customer;
import com.stevo.bankbackend2.repository.CustomerRepository;

@Component
public class BankbackendUsernamePwdAuthenticationProvider implements AuthenticationProvider {

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  CustomerRepository customerRepository;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String username = authentication.getName();
    String pwd = authentication.getCredentials().toString();

    Customer customer = customerRepository.findByEmail(username).get(0);

    if(passwordEncoder.matches(pwd, customer.getPwd()) == false) {
      throw new BadCredentialsException("Wrong password");
    }
    
    List<GrantedAuthority> grantedAuthorities = getGrantedAuthorities(((Customer) customer).getAuthorities());
    System.out.println("grantedAuthorities: " + grantedAuthorities);

    return new UsernamePasswordAuthenticationToken( username, pwd, getGrantedAuthorities(((Customer) customer).getAuthorities()) );
  
  }

  private List<GrantedAuthority> getGrantedAuthorities(Set<Authority> authorities) {
    List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
    for(Authority authority : authorities) {
      grantedAuthorities.add(new SimpleGrantedAuthority(authority.getName()));
    }
    return grantedAuthorities;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return(UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication)); // checks if the authentication is of type UsernamePasswordAuthenticationToken
  }
  
}
