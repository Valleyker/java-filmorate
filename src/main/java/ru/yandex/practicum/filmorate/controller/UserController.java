package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/users")

public class UserController {

    private HashMap<Integer, User> users = new HashMap<>();

    @GetMapping()
    public ArrayList<User> findAll() {
        ArrayList<User> values = new ArrayList<>(users.values());
        log.info("Получен запрос получение всех пользователей. Количество пользователей - {}", users.size());
        return values;
    }


    @PostMapping()
    public User create(@RequestBody User user) throws UserException {
        checkUser(user);
        user.setId(users.size() + 1);
        users.put(user.getId(), user);
        log.info("Получен запрос на добавление пользователя с id- {}.", user.getId());
        return user;
    }

    @PutMapping()
    public User update(@RequestBody User user) throws UserException {
        if (users.containsKey(user.getId())) {
            checkUser(user);
            users.put(user.getId(), user);
            log.info("Получен запрос на изменение пользователя с id- {}.", user.getId());
            return user;
        } else {
            throw new UserException("id не существует");
        }
    }

    private void checkUser(User user) throws UserException {
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new UserException("Адрес электронной почты не может быть пустым.");
        }
        if (!user.getEmail().contains("@")) {
            throw new UserException("В почте отсутствует знак @");
        }
        if (user.getLogin() == null || user.getLogin().trim().isEmpty() || user.getLogin().contains(" ")) {
            throw new UserException("Логин не может быть пустым или содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new UserException(String.format("установленная дата в будущем"));
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}
