package ru.joor.songbracket.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component
public class RedirectHandler {

    @Autowired
    RestTemplate restTemplate;

    public String redirectIf3xx(ResponseEntity<String> response) {
        HttpStatus statusCode = response.getStatusCode();
        if (statusCode.is3xxRedirection()) {
            // Перенаправление произошло
            URI redirectUrl = response.getHeaders().getLocation();
            // Выполните запрос к новому URL
            assert redirectUrl != null;
            ResponseEntity<String> redirectedResponse = restTemplate.exchange(redirectUrl, HttpMethod.GET, null, String.class);
            return redirectedResponse.getBody();
        }
        return response.getBody();
    }
}
