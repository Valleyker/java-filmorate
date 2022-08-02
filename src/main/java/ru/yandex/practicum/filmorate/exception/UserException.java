package ru.yandex.practicum.filmorate.exception;

import java.io.IOException;

public class UserException extends IOException {
    public UserException(String message) {
        super(message);
    }
}