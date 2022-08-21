package ru.yandex.practicum.filmorate.storage.like;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
public class LikeDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addLike(long filmId, long userId) {
        String sql = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    public void deleteLike(long filmId, long userId) {
        String sql = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }

    public Collection<Film> getPopularFilms(long count) {
        List<Film> films = new ArrayList<>();
        String sql = "SELECT f.film_id, f.name, f.description, f.release_date, f.duration, f.mpa_id, " +
                "COUNT(u.user_id) AS top_liked FROM films f " +
                "LEFT OUTER JOIN likes U ON f.film_id = u.film_id " +
                "GROUP BY f.film_id ORDER BY top_liked DESC LIMIT ?";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sql, count);
        while (srs.next()) {
            int mpaId = srs.getInt("mpa_id");
            String mpaName = "SELECT mpa_id, name FROM mpa WHERE mpa_id = ?";
            Mpa mpa = jdbcTemplate.query(mpaName, FilmDbStorage::makeMpa, mpaId).get(0);
            Film film = new Film();
            film.setId(srs.getLong("film_id"));
            film.setName(srs.getString("name"));
            film.setDescription(srs.getString("description"));
            film.setDuration(srs.getInt("duration"));
            film.setReleaseDate(srs.getDate("release_date").toLocalDate());
            film.setMpa(mpa);
            films.add(film);
        }
        return films;
    }
}
