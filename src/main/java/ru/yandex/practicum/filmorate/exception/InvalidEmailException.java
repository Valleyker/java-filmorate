package ru.yandex.practicum.filmorate.exception;

import java.io.IOException;

public class InvalidEmailException extends IOException {
    public InvalidEmailException(String message) {
        super(message);
    }
}


