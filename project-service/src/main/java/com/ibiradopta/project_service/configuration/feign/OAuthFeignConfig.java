package com.ibiradopta.project_service.configuration.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@Configuration
public class OAuthFeignConfig {

@Bean
  public RequestInterceptor requestInterceptor() {
    return new RequestInterceptor() {
      @Override
      public void apply(RequestTemplate requestTemplate) {
        //Obtengo el token JWT desde el contexto de seguridad
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        if (authenticationToken != null) {
          Jwt jwt = authenticationToken.getToken();
          String token = jwt.getTokenValue();

          //Agrego el token JWT al header de la petición
          requestTemplate.header("Authorization", "Bearer " + token);
        }
      }
    };
  }

}
