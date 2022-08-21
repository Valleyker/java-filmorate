package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Repository
public class MpaDbStorage {

    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Collection<Mpa> findAll() {
        final String sqlQuery = "SELECT * FROM mpa";
        final List<Mpa> mpa = jdbcTemplate.query(sqlQuery, MpaDbStorage::makeMpa);
        return mpa;
    }

    public Mpa getMpaById(int id) {
        final String sqlQuery = "SELECT * FROM mpa WHERE mpa_id = ?";
        final List<Mpa> mpa = jdbcTemplate.query(sqlQuery, MpaDbStorage::makeMpa, id);
        if (mpa == null || mpa.isEmpty()) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "MPA not found");
        }
        return mpa.get(0);
    }


    static Mpa makeMpa(ResultSet rs, int rowNum) throws SQLException {
        return new Mpa(rs.getInt("mpa_id"),
                rs.getString("name")
        );
    }
}
