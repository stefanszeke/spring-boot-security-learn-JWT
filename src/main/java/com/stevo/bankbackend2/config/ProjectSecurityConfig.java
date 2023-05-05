package com.stevo.bankbackend2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;

import com.stevo.bankbackend2.filter.AuthLoggingAfterFilter;
import com.stevo.bankbackend2.filter.AuthLoggingAtFilter;
import com.stevo.bankbackend2.filter.RequestValidationBeforeFilter;
import com.stevo.bankbackend2.filter.csrfCookieFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class ProjectSecurityConfig {

  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

    CsrfTokenRequestAttributeHandler csrfTokenHandler = new CsrfTokenRequestAttributeHandler();
    csrfTokenHandler.setCsrfRequestAttributeName("_csrf");

    http
        .securityContext(securityContext -> securityContext.requireExplicitSave(false))
        .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))

        .cors(cors -> {
          cors.configurationSource(request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.addAllowedOrigin("http://localhost:4200");
            config.addAllowedMethod("*");
            config.addAllowedHeader("*");
            config.setAllowCredentials(true);
            config.setMaxAge(3600L);
            return config;
          });
        })

        .csrf(csrf -> csrf
            .csrfTokenRequestHandler(csrfTokenHandler)
            .ignoringRequestMatchers("/register")
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))

        .addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class)
        .addFilterAt(new AuthLoggingAtFilter(), BasicAuthenticationFilter.class)
        .addFilterAfter(new csrfCookieFilter(), BasicAuthenticationFilter.class)
        .addFilterAfter(new AuthLoggingAfterFilter(), BasicAuthenticationFilter.class)


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
