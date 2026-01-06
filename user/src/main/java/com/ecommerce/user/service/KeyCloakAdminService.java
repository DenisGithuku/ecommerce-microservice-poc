package com.ecommerce.user.service;

import com.ecommerce.user.dto.CreateUserRequestDto;
import jakarta.ws.rs.core.Link;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KeyCloakAdminService {
    @Value("${keycloak.admin.username}")
    private String username;

    @Value("${keycloak.admin.password}")
    private String password;

    @Value("${keycloak.admin.server-url}")
    private String keycloakServerUrl;

    @Value("${keycloak.admin.realm}")
    private String realm;

    @Value("${keycloak.admin.client-id}")
    private String clientId;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getAccessToken() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("username", username);
        params.add("password", password);
        params.add("grant_type", "password");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        String url = keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";
        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
        return (String) response.getBody().get("access_token");
    }

    public String createUser(String accessToken, CreateUserRequestDto userRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        Map<String, Object> userPayload = new HashMap<>();
        userPayload.put("username", userRequest.username());
        userPayload.put("email", userRequest.email());
        userPayload.put("enabled", true);
        userPayload.put("firstName", userRequest.firstName());
        userPayload.put("lastName", userRequest.lastName());
        Map<String, Object> credentials = new HashMap<>();
        credentials.put("type", "password");
        credentials.put("value", userRequest.password());
        credentials.put("temporary", false);

        userPayload.put("credentials", List.of(credentials));
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(userPayload, headers);
        String url = keycloakServerUrl + "/admin/realms" + realm + "/users";
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        if (HttpStatus.CREATED == response.getStatusCode()) {
            throw new RuntimeException("Could not create a user in keycloak" + response.getBody());
        }
        // Extract keycloak user id
        URI location = response.getHeaders().getLocation();
        if (location == null) {
            throw new RuntimeException("Keycloak did not return any Location header");
        }
        String path = location.getPath();
        return path.substring(path.lastIndexOf("/") + 1);
    }
}
