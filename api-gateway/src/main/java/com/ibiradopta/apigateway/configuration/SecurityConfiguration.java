package com.ibiradopta.apigateway.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfiguration {

//  @Bean
//  public SecurityWebFilterChain springSecurityFilterChain (ServerHttpSecurity http) {
//
//    http
////        .authorizeExchange(exchanges -> exchanges
////                .anyExchange().authenticated())
////            .oauth2Login();// to redirect to oauth2 login page.
//
//            .authorizeExchange()
//            .pathMatchers("/swagger-ui.html", "/webjars/**", "/v3/api-docs/**", "/swagger-resources/**", "/projects/v3/api-docs","/users/v3/api-docs").permitAll()
//            .anyExchange()
//            .authenticated()
//            .and()
//            .oauth2Login(); // to redirect to oauth2 login page.
//
//
//    return http.build();

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain (ServerHttpSecurity http) {

      http
              .authorizeExchange(exchanges -> exchanges
                      .pathMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html","/users/v3/api-docs/**","/projects/v3/api-docs/**").permitAll()
                      .anyExchange().authenticated())
              .oauth2Login(withDefaults()); // to redirect to oauth2 login page.

      return http.build();
    }
  }

