package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.FilmException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidationFilmTests {

    @Test()
    void CheckNullNameOfTheFilm() {
        FilmController filmController = new FilmController();
        Film film = Film.builder().id(1).description("test1").releaseDate(LocalDate.parse("2020-01-02")).duration(120).build();
        assertThrows(FilmException.class, () -> filmController.create(film));

    }

    @Test()
    void CheckDescriptionOfTheFilmWithMoreThan250Symbols() {
        FilmController filmController = new FilmController();
        Film film = Film.builder().id(1).description("HtQuZcQIeLx3hs2FssI389bF1wy1IiGGhx5zRJQAgLDNXMDDxlcxfSnNPQjTLBT8u3U57nTeC3t7kN6xnKr7nQfFaNJ0bXwdkdq2ethlXBuU71SGD5G9snOqDgG0lidNLgRYNqi6lHs443aXNtQYRyhg3sQ8djzetKAmBHtHxSnMbQzJa1GkcpMdtPtAajzcF8fYhZG2ftZyDBy3UM1g5LCR36cx3HBOVq24MGnGqxHURspeWi").releaseDate(LocalDate.parse("2020-01-02")).duration(120).name("LOL").build();
        assertThrows(FilmException.class, () -> filmController.create(film));

    }

    @Test()
    void CheckDescriptionOfTheFilmWith200Symbols() throws FilmException {
        FilmController filmController = new FilmController();
        Film film = Film.builder().id(1).description("qlybubXPs7ehi0s2Q4BHxvOFf6Lbspa4EnnuEoDJe0pMTX1BpL6iBgdLcqX2Jgwle67dTCqB9vT96HhlxepBc155BujmrUbUPdxlhBRBZ21xAFHKeHfrTx3M9XgOU32mExQyJvinYvjOJIhvL306UUkrtFbmP7JmOchxRslcR31VAGBUZRY18qd2n46JoFO5pE9dJf14").releaseDate(LocalDate.parse("2020-01-02")).duration(120).name("LOL").build();
        filmController.create(film);
        assertEquals(200, filmController.findAll().get(0).getDescription().length());
    }

    @Test()
    void CheckReleaseDateBefore1895() {
        FilmController filmController = new FilmController();
        Film film = Film.builder().id(1).description("test").releaseDate(LocalDate.parse("1895-12-27")).duration(120).name("LOL").build();
        assertThrows(FilmException.class, () -> filmController.create(film));
    }

    @Test()
    void CheckReleaseDateOn1895() throws FilmException {
        FilmController filmController = new FilmController();
        Film film = Film.builder().id(1).description("test").releaseDate(LocalDate.parse("1895-12-28")).duration(120).name("LOL").build();
        filmController.create(film);
        assertEquals(LocalDate.parse("1895-12-28"), film.getReleaseDate());
    }

    @Test()
    void CheckDurationMinus() throws FilmException {
        FilmController filmController = new FilmController();
        Film film = Film.builder().id(1).description("test").releaseDate(LocalDate.parse("1995-12-28")).duration(-120).name("LOL").build();
        assertThrows(FilmException.class, () -> filmController.create(film));
    }

    @Test()
    void CheckDurationZero() throws FilmException {
        FilmController filmController = new FilmController();
        Film film = Film.builder().id(1).description("test").releaseDate(LocalDate.parse("1995-12-28")).duration(0).name("LOL").build();
        assertThrows(FilmException.class, () -> filmController.create(film));
    }

    @Test()
    void CheckDurationPlus() throws FilmException {
        FilmController filmController = new FilmController();
        Film film = Film.builder().id(1).description("test").releaseDate(LocalDate.parse("1995-12-28")).duration(120).name("LOL").build();
        filmController.create(film);
        assertEquals(120, film.getDuration());
    }

}
