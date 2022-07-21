package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private HashMap<String, Film> films = new HashMap<>();

    @GetMapping()
    public ArrayList<Film> findAll() {
        log.info("Получен запрос получение всех фильмов. Количество фильмов - {}", films.size());
        for (Film user : films.values()) {
            System.out.println(user.toString());
        }
        ArrayList<Film> values = new ArrayList<>(films.values());
        return values;
    }


    @PostMapping()
    public Film create(@RequestBody Film film) throws FilmException {
        checkFilm(film);
        films.put(film.getName(), film);
        log.info("Получен запрос на добавление фильма c названием- {}.", film.getName());
        return film;
    }

    @PutMapping()
    public Film update(@RequestBody Film film) throws FilmException {
        checkFilm(film);
        films.put(film.getName(), film);
        log.info("Получен запрос на изменение фильма c названием- {}.", film.getName());
        return film;
    }

    private void checkFilm(Film film) throws FilmException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new FilmException("Дата должна быть после 1895-12-28");
        }
        if (film.getDescription().length() > 200) {
            throw new FilmException("количество символом больше 200");
        }
        if (film.getName() == null || film.getName().trim().isEmpty()) {
            throw new FilmException("Название фильма не может быть пустым.");
        }
        if (film.getDuration() <= 0) {
            throw new FilmException("Продолжительность фильма должна быть положительной.");
        }
    }

}