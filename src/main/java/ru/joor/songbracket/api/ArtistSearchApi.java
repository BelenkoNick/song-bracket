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
import ru.joor.songbracket.entity.Artist;

@Component
@AllArgsConstructor
public class ArtistSearchApi {

    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;
    private RedirectHandler redirectHandler;

    private static final String SEARCH_API_URL = "http://api.genius.com/search";

    public Artist getArtist(String searchTerm) throws JsonProcessingException {

        // Создание URL и параметров запроса
        String builder = UriComponentsBuilder.fromUriString(SEARCH_API_URL)
                .queryParam("q", searchTerm)
                .toUriString();
        // Выполнение запроса
        ResponseEntity<String> response = restTemplate.exchange(builder, HttpMethod.GET, null, String.class);

        // Перенаправление запроса при необходимости
        String responseBody = redirectHandler.redirectIf3xx(response);

        // Объект артиста
        Artist artist = new Artist();

        //Считывание ответа
        JsonNode rootNode = objectMapper.readTree(responseBody);
        JsonNode hitsNode = rootNode.get("response").get("hits");
        if (hitsNode.isArray()) {
            JsonNode firstHitNode = hitsNode.iterator().next();
            JsonNode artistNode = firstHitNode.get("result").get("primary_artist");
            if (artistNode != null) {
                // Получение нужных полей из artistNode
                artist.setArtistName( artistNode.get("name").asText());
                artist.setId(artistNode.get("id").asText());
                System.out.println("Artist Names: " + artist.getArtistName());
                System.out.println("Artist Id: " + artist.getId());
            }
        }
        return artist;
    }
}
