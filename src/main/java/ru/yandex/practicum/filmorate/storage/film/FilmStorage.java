package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;

public interface FilmStorage {

    ArrayList<Film> findAll();

    Film create(Film film);

    Film update(Film film);

    Film getById(long id);

    void deleteFilm(long id);
}
