package ru.yandex.practicum.filmorate.exception;

import java.io.IOException;

public class FilmException extends IOException {
    public FilmException(String message) {
        super(message);
    }
}

