package com.ibiradopta.apigateway.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfiguration {



    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

        http
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Allow all OPTIONS requests
                        .pathMatchers("/payments/notify/**").permitAll()  // Permitir acceso no autenticado solo a este endpoint
                        .pathMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html",
                                "/users/v3/api-docs/**", "/projects/v3/api-docs/**",
                                "/projects/v3/api-docs/**",
                                "/users/**","/users/update","/projects/**","/payments/**").permitAll() // Allow specific endpoints
                        .anyExchange().authenticated())
                .csrf((csrf) -> csrf.disable()) // Disable CSRF
                .oauth2Login(withDefaults()); // Redirect to OAuth2 login page for other endpoints

        return http.build();
    }
  }

