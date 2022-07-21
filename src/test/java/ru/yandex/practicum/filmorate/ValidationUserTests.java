package ru.yandex.practicum.filmorate;

import com.sun.media.sound.InvalidDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.InvalidEmailException;
import ru.yandex.practicum.filmorate.exception.InvalidUserDateException;
import ru.yandex.practicum.filmorate.exception.UserLoginException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidationUserTests {

    @Test
    void EmailEmptyTest() {
        UserController userController = new UserController();
        User user = User.builder().id(1).login("test1").name("test1").birthday(LocalDate.parse("2020-01-02")).build();
        Throwable throwable =  assertThrows(Throwable.class, () -> userController.create(user));
        assertEquals(InvalidEmailException.class, throwable.getClass());
    }

    @Test
    void MissingAtSymbolTest() {
        UserController userController = new UserController();
        User user = User.builder().id(1).email("v4lgerasimovyandex.ru").login("test1").name("test1").birthday(LocalDate.parse("2020-01-02")).build();
        Throwable throwable =  assertThrows(Throwable.class, () -> userController.create(user));
        assertEquals(InvalidEmailException.class, throwable.getClass());
    }

    @Test
    void LoginEmptyTest() {
        UserController userController = new UserController();
        User user = User.builder().id(1).email("v4lgerasimov@yandex.ru").name("test1").birthday(LocalDate.parse("2020-01-02")).build();
        Throwable throwable =  assertThrows(Throwable.class, () -> userController.create(user));
        assertEquals(UserLoginException.class, throwable.getClass());
    }

    @Test
    void LoginSpaceTest() {
        UserController userController = new UserController();
        User user = User.builder().id(1).email("v4lgerasimov@yandex.ru").login("Ivan Gerasimov").name("test1").birthday(LocalDate.parse("2020-01-02")).build();
        Throwable throwable =  assertThrows(Throwable.class, () -> userController.create(user));
        assertEquals(UserLoginException.class, throwable.getClass());
    }

    @Test
    void NameEmptyTests() throws InvalidDataException, InvalidEmailException, UserLoginException, InvalidUserDateException {
        UserController userController = new UserController();
        User user = User.builder().id(1).email("v4lgerasimov@yandex.ru").login("IvanGerasimov").birthday(LocalDate.parse("2020-01-02")).build();
        userController.create(user);
        assertEquals(user.getLogin(), userController.findAll().get(0).getName());
    }

    @Test
    void BirthdayTest() {
        UserController userController = new UserController();
        User user = User.builder().id(1).email("v4lgerasimov@yandex.ru").login("IvanGerasimov").name("test1").birthday(LocalDate.parse("2025-01-02")).build();
        Throwable throwable = assertThrows(Throwable.class, () -> userController.create(user));
        assertEquals(InvalidUserDateException.class, throwable.getClass());
    }

}
