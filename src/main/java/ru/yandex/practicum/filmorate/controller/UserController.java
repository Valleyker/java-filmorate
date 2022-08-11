package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ArrayList<User> findAll() {
        log.info("GET - {} users", userService.findAll().size());
        return new ArrayList<>(userService.findAll());
    }

    @PostMapping
    public User createUser(@RequestBody User user) throws ValidationException {
        log.info("POST - user with id: {}", user.getId());
        return userService.create(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) throws ValidationException {
        log.info("PUT - user with id: {}", user.getId());
        return userService.update(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") long userId) {
        log.info("GET user by id: {}", userId);
        return userService.getById(userId);
    }

    @GetMapping("/{id}/friends")
    public Set<User> getListFriends(@PathVariable("id") long userId) {
        log.info("GET - list friends user with id: {}", userId);
        return userService.getListFriends(userId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<HttpStatus> addInFriends(@PathVariable("id") long userId,
                                                   @PathVariable("friendId") long friendId) {
        log.info("PUT - add user id: {} in friend id: {}", userId, friendId);
        userService.addInFriend(userId, friendId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFromFriends(@PathVariable("id") long userId,
                                  @PathVariable("friendId") long friendId) {
        log.info("DELETE - user id: {} from friend id: {}", userId, friendId);
        userService.deleteFromFriends(userId, friendId);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public ArrayList<User> getListCommonFriends(@PathVariable("id") long userId,
                                                @PathVariable("otherId") long otherId) {
        log.info("GET - {} common friends with user", userService.getListCommonFriends(userId, otherId).size());
        return new ArrayList<>(userService.getListCommonFriends(userId, otherId));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(final ValidationException e) {
        log.info("400 - {}", e.getMessage());
        return new ErrorResponse(String.format("Ошибка с полем \"%s\".", e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        log.info("404 - {}", e.getMessage());
        return new ErrorResponse(String.format("Ошибка с полем \"%s\".", e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalServerErrorException(final Exception e) {
        log.info("500 - {}", e.getMessage());
        return new ErrorResponse(String.format("Ошибка с полем \"%s\".", e.getMessage()));
    }
}
