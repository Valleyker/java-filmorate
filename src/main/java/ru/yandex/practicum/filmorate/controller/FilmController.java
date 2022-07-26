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

    private static final LocalDate MOVIE_BIRTHDAY  = LocalDate.of(1895, 12, 28);
    private HashMap<Integer, Film> films = new HashMap<>();

    @GetMapping()
    public ArrayList<Film> findAll() {
        ArrayList<Film> values = new ArrayList<>(films.values());
        log.info("Получен запрос получение всех фильмов. Количество фильмов - {}", films.size());
        return values;
    }


    @PostMapping()
    public Film create(@RequestBody Film film) throws FilmException {
        checkFilm(film);
        film.setId(films.size() + 1);
        films.put(film.getId(), film);
        log.info("Получен запрос на добавление фильма c id- {}.", film.getId());
        return film;
    }

    @PutMapping()
    public Film update(@RequestBody Film film) throws FilmException {
        if (films.containsKey(film.getId())) {
            checkFilm(film);
            films.put(film.getId(), film);
            log.info("Получен запрос на изменение фильма c id- {}.", film.getId());
            return film;
        } else {
            throw new FilmException("id не существует");
        }
    }

    private void checkFilm(Film film) throws FilmException {
        if (film.getReleaseDate().isBefore(MOVIE_BIRTHDAY)) {
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