package ru.joor.songbracket.utils;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Configuration
@AllArgsConstructor
public class ConnectionUtils {

    private String accessToken;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            URI[] modifiedUri = new URI[1];
            modifiedUri[0] = request.getURI();
            String query = modifiedUri[0].getQuery();
            if (query == null || !query.contains("access_token=")) {
                UriComponentsBuilder builder = UriComponentsBuilder.fromUri(modifiedUri[0]);
                builder.queryParam("access_token", accessToken);
                modifiedUri[0] = builder.build().toUri();
                System.out.println(builder.toUriString());
            }
            HttpRequest modifiedRequest = new HttpRequestWrapper(request) {
                @Override
                public URI getURI() {
                    return modifiedUri[0];
                }
            };
            return execution.execute(modifiedRequest, body);
        });
        return restTemplate;
    }
}
