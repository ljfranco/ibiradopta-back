package com.ibiradopta.project_service.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class OAuth2ResourceServerSecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(HttpMethod.GET, "/projects/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/mercadopago/notify").permitAll()
                        .requestMatchers("/projects/swagger-ui/**"
                                , "/projects/v3/api-docs/**"
                                , "/projects/swagger-ui.html"
                                , "/projects/swagger-ui/**"
                                , "/projects/swagger-ui.html"
                                , "/projects/webjars/**"
                                , "/projects/swagger-resources/**"
                                ,"/mercadopago/**"
                                ).permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(new KeyCloakJwtAuthenticationConverter())
                        )

                );


        return http.build();
    }

}
