package ru.joor.songbracket.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.joor.songbracket.api.ArtistSearchApi;
import ru.joor.songbracket.api.SongsSearchApi;
import ru.joor.songbracket.entity.Artist;

import java.util.Scanner;

@Service
public class GameLoop {

    @Autowired
    ArtistSearchApi artistSearchApi;
    @Autowired
    SongsSearchApi songsSearchApi;

    private final Scanner scanner = new Scanner(System.in);

    public void play() throws JsonProcessingException {
        System.out.println("Введите своего испольнителя:");
        String artistToSearch = scanner.nextLine();
        Artist artist = artistSearchApi.getArtist(artistToSearch.replaceAll("\\s", ""));
        songsSearchApi.getSongs(artist.getId(), artist.getArtistName());
    }
}
