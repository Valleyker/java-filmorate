package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.util.ArrayList;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public ArrayList<Film> findAll() {
        log.info("GET - all films");
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable long id) {
        log.info("GET - film by id");
        return filmService.getById(id);
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) throws ValidationException {
        log.info("POST - create film");
        return filmService.create(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        log.info("PUT - update film");
        return filmService.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") long filmId,
                        @PathVariable long userId) {
        log.info("PUT - user set liked the film");
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") long filmId,
                           @PathVariable long userId) {
        log.info("DELETE - user deleted like from the film");
        filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public ArrayList<Film> getListPopularFilm(@RequestParam(defaultValue = "10") int count) {
        log.info("GET - list popular film(size = count)");
        return new ArrayList<>(filmService.getListPopularFilm(count));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(final ValidationException e) {
        log.info("400 - {}", e.getMessage());
        return new ErrorResponse(String.format("Ошибка с полем \"%s\".", e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        log.info("404 - {}", e.getMessage());
        return new ErrorResponse(String.format("Ошибка с полем \"%s\".", e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalServerErrorException(final Exception e) {
        log.info("500 - {}", e.getMessage());
        return new ErrorResponse(String.format("Ошибка с полем \"%s\".", e.getMessage()));
    }

}
