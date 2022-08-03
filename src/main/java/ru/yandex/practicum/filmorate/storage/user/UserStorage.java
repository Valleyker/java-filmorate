package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.Collection;

public interface UserStorage {

    ArrayList<User> findAll();

    User create(User user) throws ValidationException;

    User update(User user) throws ValidationException;

    User getById(long id);

    boolean contains(long id);
}