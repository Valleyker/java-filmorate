package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ArrayList<User> findAll() {
        final String sqlQuery = "SELECT * FROM users ";
        final List<User> users = jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser);
        return new ArrayList<>(users);
    }

    @Override
    public User create(User user) throws ValidationException {
        String sqlQuery = "INSERT INTO users (email, name, login, birthday) VALUES  (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getLogin());
            final LocalDate birthday = user.getBirthday();
            if (birthday == null) {
                stmt.setNull(4, Types.DATE);
            } else {
                stmt.setDate(4, Date.valueOf(birthday));
            }
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    @Override
    public User update(User user) throws ValidationException {
        getById(user.getId());
        String sqlQuery = "UPDATE users SET " +
                "email = ?, name = ?, login = ?,birthday = ? " +
                "WHERE user_id = ?";
        jdbcTemplate.update(sqlQuery
                , user.getEmail()
                , user.getName()
                , user.getLogin()
                , user.getBirthday()
                , user.getId());
        return user;
    }

    @Override
    public User getById(long id) {
        final String sqlQuery = "SELECT * FROM users WHERE user_id = ?";
        final List<User> users = jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser, id);
        if (users == null || users.isEmpty()) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "User not found");
        }
        return users.get(0);
    }

    @Override
    public void deleteUser(long id) {
        String sqlQuery = "DELETE FROM users WHERE user_id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }


    static User makeUser(ResultSet rs, int rowNum) throws SQLException {
        return new User(rs.getLong("user_id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getDate("birthday").toLocalDate());
    }

}
