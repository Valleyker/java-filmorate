package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friends.FriendDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private final FriendDbStorage friendDbStorage;

    @Autowired
    public UserService(UserStorage userStorage, FriendDbStorage friendDbStorage) {
        this.userStorage = userStorage;
        this.friendDbStorage = friendDbStorage;
    }

    public User create(User user) throws ValidationException {
        validate(user);
        return userStorage.create(user);
    }

    public User update(User user) throws ValidationException {
        validate(user);
        return userStorage.update(user);
    }

    public ArrayList<User> findAll() {
        return userStorage.findAll();
    }

    public User getById(long userId) {
        return userStorage.getById(userId);
    }

    public void addInFriend(long userId, long friendId) {
        userStorage.getById(userId);
        userStorage.getById(friendId);
        friendDbStorage.addFriend(userId, friendId);
    }

    public Collection<User> getListFriends(long userId) {
        return friendDbStorage.getAllFriends(userId);
    }

    public void deleteFromFriends(long userId, long friendId) {
        userStorage.getById(userId);
        userStorage.getById(friendId);
        friendDbStorage.deleteFriend(userId, friendId);
    }

    public List<User> getListCommonFriends(long user1, long user2) {
        return (List<User>) friendDbStorage.getJointFriends(user1, user2);
    }

    public void deleteFriend(long id, long friendId) {
        userStorage.getById(id);
        userStorage.getById(friendId);
        friendDbStorage.deleteFriend(id, friendId);
    }

    public void deleteUser(long id) {
        userStorage.deleteUser(id);
    }

    private void validate(User user) throws ValidationException {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            log.debug("Адрес электронной почты пуст/не содержит @");
            throw new ValidationException(HttpStatus.BAD_REQUEST ,"Проверьте адрес электронной почты.");
        }
        if (user.getLogin() == null || user.getLogin().contains(" ")) {
            log.debug("Логин содержит пробелы/пустой");
            throw new ValidationException(HttpStatus.BAD_REQUEST ,"Логин не может содержать пробелы или быть пустым");
        }
        if (user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Дата рождения в будущем");
            throw new ValidationException(HttpStatus.BAD_REQUEST ,"Дата рождения не может быть в будущем.");
        }
    }

}
