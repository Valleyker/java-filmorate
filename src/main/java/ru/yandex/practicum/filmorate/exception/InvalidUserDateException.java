package ru.yandex.practicum.filmorate.exception;

        import java.io.IOException;

public class InvalidUserDateException extends IOException {
    public InvalidUserDateException(String message) {
        super(message);
    }
}