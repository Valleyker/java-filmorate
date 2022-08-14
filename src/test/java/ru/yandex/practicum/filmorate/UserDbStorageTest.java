package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final UserService userService;

    @Test
    public void emailEmptyTest() {
        User user = new User(1, "", "login", "test",
                LocalDate.of(2003, 11, 11));
        assertThrows(ValidationException.class, () -> userService.create(user));
    }

    @Test
    public void missingAtSymbolTest() {
        User user = new User(1, "asdas", "login", "test",
                LocalDate.of(2003, 11, 11));
        assertThrows(ValidationException.class, () -> userService.create(user));
    }

    @Test
    public void loginEmptyTest() {
        User user = new User(1, "asdas@mail.ru", null, "test",
                LocalDate.of(2003, 11, 11));
        assertThrows(ValidationException.class, () -> userService.create(user));
    }

    @Test
    public void loginSpaceTest() {
        User user = new User(1, "asdas@mail.ru", " ", "test",
                LocalDate.of(2003, 11, 11));
        assertThrows(ValidationException.class, () -> userService.create(user));
    }

    @Test
    public void nameEmptyTests() {
        User user = new User(1, "asdas@mail.ru", "asd", null,
                LocalDate.of(2003, 11, 11));
        userService.create(user);
        assertEquals(user.getLogin(), userService.getById(1).getName());
    }

    @Test
    public void birthdayTest() {
        User user = new User(1, "asdas@mail.ru", "asdfasd", "asd",
                LocalDate.of(2043, 11, 11));
        assertThrows(ValidationException.class, () -> userService.create(user));
    }
}
