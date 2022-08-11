package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;

public interface FilmStorage {

    ArrayList<Film> findAll();

    Film create(Film film) throws ValidationException;

    Film update(Film film) throws ValidationException;

    Film getById(long id);

    boolean contains(long id);

}
