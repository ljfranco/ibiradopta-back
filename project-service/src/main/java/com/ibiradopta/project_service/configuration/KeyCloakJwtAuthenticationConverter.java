package com.ibiradopta.project_service.configuration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class KeyCloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private final JwtGrantedAuthoritiesConverter defaultGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    private static Collection<? extends GrantedAuthority> extractResourceRoles(final Jwt jwt) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Set<GrantedAuthority> resourcesRoles = new HashSet<>(extractRolesRealmAccess("realm_access", objectMapper.readTree(objectMapper.writeValueAsString(jwt)).get("claims")));
        return resourcesRoles;
    }


    private static List<GrantedAuthority> extractRolesRealmAccess(String route, JsonNode jwt) {
        Set<String> rolesWithPrefix = new HashSet<>();
        jwt.path(route).path("roles").elements().forEachRemaining((r) -> {
            rolesWithPrefix.add("ROLE_" + r.asText());
        });
        List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList((String[])rolesWithPrefix.toArray(new String[0]));
        return authorityList;
    }


    public KeyCloakJwtAuthenticationConverter() {
    }

    public AbstractAuthenticationToken convert(final Jwt source) {
        Collection<GrantedAuthority> authorities = null;

        try {
            authorities = this.getGrantedAuthorities(source);
        } catch (JsonProcessingException var4) {
            JsonProcessingException e = var4;
            e.printStackTrace();
        }

        return new JwtAuthenticationToken(source, authorities);
    }

    public Collection getGrantedAuthorities(Jwt source) throws JsonProcessingException {
        return Stream.concat(this.defaultGrantedAuthoritiesConverter.convert(source).stream(), extractResourceRoles(source).stream()).collect(Collectors.toSet());
    }
}