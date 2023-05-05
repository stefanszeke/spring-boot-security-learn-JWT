package com.stevo.bankbackend2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.stevo.bankbackend2.filter.JWTTokenGeneratorFilter;
import com.stevo.bankbackend2.filter.JWTTokenValidatorFilter;


import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Arrays;

@Configuration
public class ProjectSecurityConfig {



  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

    http
        .sessionManagement(
            sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // no session

        .cors(cors -> {
          cors.configurationSource(request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.addAllowedOrigin("http://localhost:4200");
            config.addAllowedMethod("*");
            config.addAllowedHeader("*");
            config.setAllowCredentials(true);
            config.setMaxAge(3600L);
            config.setExposedHeaders(Arrays.asList("Authorization")); // for JWT
            return config;
          });
        })

        // jwt
        .addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class)
        .addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class)

        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/myAccount").hasRole("USER")
            .requestMatchers("/myBalance").hasAnyRole("USER", "ADMIN")
            .requestMatchers("/myLoans").hasRole("USER")
            .requestMatchers("/myCards").hasRole("USERS")

            .requestMatchers("/user").authenticated()
            .requestMatchers("/notices", "/register").permitAll()
            .anyRequest().authenticated())

        .formLogin(withDefaults())
        .httpBasic(withDefaults());


    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  // Authorization
  // .requestMatchers("/myAccount").hasAuthority("VIEWACCOUNT")
  // .requestMatchers("/myBalance").hasAnyAuthority("VIEWACCOUNT", "VIEWBALANCE")
  // .requestMatchers("/myLoans").hasAuthority( "VIEWLOANS")
  // .requestMatchers("/myCards").hasAuthority("VIEWCARDS")

  // authentication
  // .requestMatchers("/myAccount", "/myBalance", "/myLoans", "/myCards",
  // "/user","/contact").authenticated()

  // @Bean
  // public InMemoryUserDetailsManager userDetailsService() {
  // UserDetails admin = User.withDefaultPasswordEncoder()
  // .username("admin")
  // .password("admin")
  // .roles("admin")
  // .build();

  // UserDetails user = User.withDefaultPasswordEncoder()
  // .username("user")
  // .password("user")
  // .roles("user")
  // .build();

  // return new InMemoryUserDetailsManager(admin, user);
  // }

  // @Bean
  // public UserDetailsService userDetailsService(DataSource dataSource) {
  // return new JdbcUserDetailsManager(dataSource);
  // }

}
