package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;

public interface UserStorage {

    ArrayList<User> findAll();

    User create(User user) ;

    User update(User user) ;

    User getById(long id);

    void deleteUser(long id);
}