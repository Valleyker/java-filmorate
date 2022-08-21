package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDBStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeDbStorage;

import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class FilmService {
    private final LikeDbStorage likeDbStorage;
    private final FilmStorage filmStorage;
    private final GenreDBStorage genreDBStorage;
    private final UserService userService;
    private static final LocalDate MOVIE_BIRTHDAY = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmService(LikeDbStorage likeDbStorage, FilmStorage filmStorage,
                       GenreDBStorage genreDBStorage, UserService userService) {
        this.likeDbStorage = likeDbStorage;
        this.filmStorage = filmStorage;
        this.genreDBStorage = genreDBStorage;
        this.userService = userService;
    }

    public ArrayList<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film getById(long filmId) {
        Film film = filmStorage.getById(filmId);
        film.setGenres(new LinkedHashSet<>(genreDBStorage.getFilmGenres(filmId)));
        return film;
    }

    public Film create(Film film) throws ValidationException {
        validate(film);
        return filmStorage.create(film);
    }

    public Film update(Film film) throws ValidationException {
        genreDBStorage.deleteFilm(film.getId());
        return filmStorage.update(film);
    }

    public void addLike(long filmId, long userId) {
        filmStorage.getById(filmId);
        userService.getById(userId);
        likeDbStorage.addLike(filmId, userId);
    }

    public void deleteLike(long filmId, long userId) {
        filmStorage.getById(filmId);
        userService.getById(userId);
        likeDbStorage.deleteLike(filmId, userId);
    }

    public List<Film> getListPopularFilm(long count) {
        return (List<Film>) likeDbStorage.getPopularFilms(count);
    }

    public void deleteFilm(long id) {
        filmStorage.deleteFilm(id);

    }

    private void validate(Film film) throws ValidationException {
        if (film.getName() == null || film.getName().isBlank()) {
            log.debug("Название фильма не указано");
            throw new ValidationException(HttpStatus.BAD_REQUEST ,"Название фильма не указано.");
        }
        if (film.getDescription().length() > 200) {
            log.debug("Описание фильма больше 200 символов");
            throw new ValidationException(HttpStatus.BAD_REQUEST ,"Описание " +
                    "фильма не должно превышать 200 символов.");
        }
        if (film.getReleaseDate().isBefore(MOVIE_BIRTHDAY)) {
            log.debug("Дата релиза раньше 28 декабря 1895 года");
            throw new ValidationException(HttpStatus.BAD_REQUEST ,"Дата релиза не может" +
                    " быть раньше 28 декабря 1895 года.");
        }
        if (film.getDuration() <= 0) {
            log.debug("Продолжительность фильма отрицательная или равна нулю");
            throw new ValidationException(HttpStatus.BAD_REQUEST ,"Продолжительность " +
                    "фильма не может быть отрицательной.");
        }
    }

}
