package ru.yandex.practicum.filmorate.exception;


import java.io.IOException;

public class UserLoginException extends IOException {
    public UserLoginException(String message) {
        super(message);
    }
}

