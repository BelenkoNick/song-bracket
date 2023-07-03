package ru.joor.songbracket.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.joor.songbracket.utils.RedirectHandler;

@Component
@AllArgsConstructor
public class SongsSearchApi {

    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;
    private RedirectHandler redirectHandler;

    private static final String SEARCH_API_URL = "http://api.genius.com/artists/{id}/songs";

    public void getSongs(String id) throws JsonProcessingException {

        // Создание URL и параметров запроса
        String builder = UriComponentsBuilder.fromUriString(SEARCH_API_URL)
                .queryParam("sort", "popularity")
                .queryParam("per_page", "50")
                .buildAndExpand(id)
                .toUriString();
        // Выполнение запроса
        ResponseEntity<String> response = restTemplate.exchange(builder, HttpMethod.GET, null, String.class);

        // Перенаправление запроса при необходимости
        String responseBody = redirectHandler.redirectIf3xx(response);
        System.out.println(responseBody);

        //Считывание ответа
        JsonNode rootNode = objectMapper.readTree(responseBody);
        JsonNode songsNode = rootNode.get("response").get("songs");
        if (songsNode.isArray()) {
            int i = 1;
            for (JsonNode songNode : songsNode) {
                // Получение нужных полей из artistNode
                String title = songNode.get("title").asText();
                String artistName = songNode.get("primary_artist").get("name").asText();
                if( artistName.contains("Kanye")) {
                    i++;
                    System.out.println("Song Title: " + title);
                    System.out.println("Artist Name: " + artistName);
                }
            }
            System.out.println(i);
        }
    }
}
