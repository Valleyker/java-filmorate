package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidationUserTests {

    @Test
    void emailEmptyTest() {
        UserService userService = new UserService(new InMemoryUserStorage());
        UserController userController = new UserController(userService);
        User user = User.builder()
                .id(1)
                .login("test1")
                .name("test1")
                .birthday(LocalDate.parse("2020-01-02"))
                .build();
        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    void missingAtSymbolTest() {
        UserService userService = new UserService(new InMemoryUserStorage());
        UserController userController = new UserController(userService);
        User user = User
                .builder()
                .id(1)
                .email("v4lgerasimovyandex.ru")
                .login("test1")
                .name("test1")
                .birthday(LocalDate.parse("2020-01-02"))
                .build();
        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    void loginEmptyTest() {
        UserService userService = new UserService(new InMemoryUserStorage());
        UserController userController = new UserController(userService);
        User user = User
                .builder()
                .id(1)
                .email("v4lgerasimov@yandex.ru")
                .name("test1")
                .birthday(LocalDate.parse("2020-01-02"))
                .build();
        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    void loginSpaceTest() {
        UserService userService = new UserService(new InMemoryUserStorage());
        UserController userController = new UserController(userService);
        User user = User
                .builder()
                .id(1)
                .email("v4lgerasimov@yandex.ru")
                .login("Ivan Gerasimov")
                .name("test1")
                .birthday(LocalDate.parse("2020-01-02"))
                .build();
        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    void nameEmptyTests() throws ValidationException {
        UserService userService = new UserService(new InMemoryUserStorage());
        UserController userController = new UserController(userService);
        User user = User
                .builder()
                .id(1)
                .email("v4lgerasimov@yandex.ru")
                .login("IvanGerasimov")
                .birthday(LocalDate.parse("2020-01-02"))
                .build();
        userController.createUser(user);
        assertEquals(user.getLogin(), userController.findAll().get(0).getName());
    }

    @Test
    void birthdayTest() {
        UserService userService = new UserService(new InMemoryUserStorage());
        UserController userController = new UserController(userService);
        User user = User
                .builder()
                .id(1)
                .email("v4lgerasimov@yandex.ru")
                .login("IvanGerasimov")
                .name("test1")
                .birthday(LocalDate.parse("2025-01-02"))
                .build();
        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

}
