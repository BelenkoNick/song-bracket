package ru.joor.songbracket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.joor.songbracket.service.GameLoop;

@SpringBootApplication
public class App {

    @Autowired
    GameLoop gameLoop;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            // Ваш код для работы с приложением через терминал
            gameLoop.play();
        };
    }
}
