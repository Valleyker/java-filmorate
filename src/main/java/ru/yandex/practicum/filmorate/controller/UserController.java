package ru.yandex.practicum.filmorate.controller;

import com.sun.media.sound.InvalidDataException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.InvalidEmailException;
import ru.yandex.practicum.filmorate.exception.InvalidUserDateException;
import ru.yandex.practicum.filmorate.exception.UserLoginException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/users")

public class UserController {

    private HashMap<String, User> users = new HashMap<>();

    @GetMapping()
    public ArrayList<User> findAll() {
        log.info("Получен запрос получение всех пользователей. Количество пользователей - {} , {}", users.size());
        for (User user : users.values()) {
            System.out.println(user.toString());
        }
        ArrayList<User> values = new ArrayList<>(users.values());
        return values;
    }


    @PostMapping()
    public User create(@RequestBody User user) throws InvalidEmailException, UserLoginException, InvalidDataException, InvalidUserDateException {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        checkEmail(user);
        checkDate(user);
        checkLogin(user);
        users.put(user.getLogin(), user);
        log.info("Получен запрос на добавление пользователя с почтой- {}.", user.getEmail());
        return user;
    }

    @PutMapping()
    public User update(@RequestBody User user) throws InvalidEmailException, UserLoginException, InvalidDataException, InvalidUserDateException {
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        checkEmail(user);
        checkDate(user);
        checkLogin(user);
        users.put(user.getLogin(), user);
        log.info("Получен запрос на добавление пользователя с почтой- {}.", user.getEmail());
        return user;
    }

    private void checkEmail(User user) throws InvalidEmailException {
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new InvalidEmailException("Адрес электронной почты не может быть пустым.");
        }
        if (!user.getEmail().contains("@")) {
            throw new InvalidEmailException("В почте отсутствует знак @");
        }
    }

    private void checkLogin(User user) throws  UserLoginException {
        if ( user.getLogin() == null || user.getLogin().trim().isEmpty() || user.getLogin().contains(" ")) {
            throw new UserLoginException("Логин не может быть пустым или содержать пробелы");
        }
    }

    private void checkDate(User user) throws InvalidUserDateException {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new InvalidUserDateException(String.format("установленная дата в будущем"));
        }
    }


}
