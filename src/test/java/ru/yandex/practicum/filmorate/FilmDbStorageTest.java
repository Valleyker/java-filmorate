package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {

    private final FilmService filmService;

    @Test
    public void checkNullNameOfTheFilm() {
        Film film = new Film(1, "", "test",
                LocalDate.of(2003, 10, 10), 120, new Mpa(1, "G"), null);
        assertThrows(ValidationException.class, () -> filmService.create(film));
    }

    @Test
    public void checkDescriptionOfTheFilmWithMoreThan250Symbols() {
        Film film = new Film(1, "test", "HtQuZcQIeLx3hs2FssI389bF1wy1IiGGhx5zRJQAgLDNXMDDx" +
                "lcxfSnNPQjTLBT8u3U57nTeC3t7kN6xnKr7nQfFaNJ0bXwdkdq2ethlXBuU71SGD5G9" +
                "snOqDgG0lidNLgRYNqi6lHs443aXNtQYRyhg3sQ8djzetKAmBHtHxSnMbQzJa1GkcpMdtPtAajz" +
                "cF8fYhZG2ftZyDBy3UM1g5LCR36cx3HBOVq24MGnGqxHURspeWiasdsaqedfsaasdfssdfsdfsdfs",
                LocalDate.of(2003, 10, 10), 85, new Mpa(1, "G"), null);
        assertThrows(ValidationException.class, () -> filmService.create(film));
    }

    @Test
    public void checkDescriptionOfTheFilmWith200Symbols() {
        Film film = new Film(1, "test", "qlybubXPs7ehi0s2Q4BHxvOFf6Lbspa4EnnuEoDJe0pMTX" +
                "1BpL6iBgdLcqX2Jgwle67dTCqB9vT96HhlxepBc155BujmrUbUPd" +
                "xlhBRBZ21xAFHKeHfrTx3M9XgOU32mExQyJvinYvjOJIhvL306UUk" +
                "rtFbmP7JmOchxRslcR31VAGBUZRY18qd2n46JoFO5xpE9dJf4",
                LocalDate.of(2003, 10, 10), 120,
                new Mpa(1, "G"), null);
        filmService.create(film);
        assertEquals(200, filmService.getById(1).getDescription().length());
    }

    @Test
    public void checkReleaseDateBefore1895() {
        Film film = new Film(1, "test", "test",
                LocalDate.of(1895, 12, 26), 120, new Mpa(1, "G"), null);
        assertThrows(ValidationException.class, () -> filmService.create(film));
    }

    @Test
    public void checkReleaseDateOn1895() {
        Film film = new Film(1, "test", "test",
                LocalDate.of(1895, 12, 28), 120, new Mpa(1, "G"), null);
        filmService.create(film);
        assertEquals(LocalDate.of(1895, 12, 28), filmService.getById(3).getReleaseDate());
    }

    @Test
    public void checkDurationMinus() {
        Film film = new Film(1, "test", "test",
                LocalDate.of(2003, 12, 29), -120, new Mpa(1, "G"), null);
        assertThrows(ValidationException.class, () -> filmService.create(film));
    }

    @Test
    public void checkDurationZero() {
        Film film = new Film(1, "test", "test",
                LocalDate.of(2003, 12, 29), 0, new Mpa(1, "G"), null);
        assertThrows(ValidationException.class, () -> filmService.create(film));
    }

    @Test
    public void checkDurationPlus() {
        Film film = new Film(1, "test", "test",
                LocalDate.of(2003, 12, 29), 120, new Mpa(1, "G"), null);
        filmService.create(film);
        assertEquals(120, filmService.getById(2).getDuration());
    }

}

