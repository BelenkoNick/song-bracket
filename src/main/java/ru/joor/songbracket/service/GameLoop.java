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

    public void play() throws JsonProcessingException {
        System.out.println("Введите своего испольнителя:");
        Scanner sc = new Scanner(System.in);
        String artistToSearch = sc.nextLine();
        Artist artist = artistSearchApi.getArtist(artistToSearch.replaceAll("\\s", ""));
        songsSearchApi.getSongs(artist.getId());
    }
}
