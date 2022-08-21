package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.genre.GenreDBStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreDBStorage genreDBStorage;


    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreDBStorage genreDBStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreDBStorage = genreDBStorage;
    }

    @Override
    public ArrayList<Film> findAll() {
        final String sqlQuery = "SELECT * FROM films";
        final List<Film> films = jdbcTemplate.query(sqlQuery, this::makeFilm);
        return new ArrayList<>(films);
    }

    @Override
    public Film create(Film film) throws ValidationException {
        String sqlQuery = "INSERT INTO films(name, description, duration, release_date, mpa_id) " +
                "VALUES  (?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setInt(3, film.getDuration());
            final LocalDate release = film.getReleaseDate();
            stmt.setDate(4, Date.valueOf(release));
            if(film.getMpa() != null){
                stmt.setInt(5, film.getMpa().getId());
            }
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());
        if (film.getGenres() != null) {
            addGenres(film);
        }
        return film;
    }

    @Override
    public Film update(Film film) throws ValidationException {
        getById(film.getId());
        genreDBStorage.deleteFilm(film.getId());
        String sqlQuery = "UPDATE films SET " +
                " name = ?, description = ?, duration = ?, release_date = ?, mpa_id = ? " +
                "WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery
                , film.getName()
                , film.getDescription()
                , film.getDuration()
                , Date.valueOf(film.getReleaseDate())
                , film.getMpa().getId()
                , film.getId());
        if (film.getGenres() != null) {
            addGenres(film);
        }
        return film;
    }

    @Override
    public Film getById(long id) {
        final String sqlQuery = "SELECT * FROM films WHERE film_id = ?";
        final List<Film> films = jdbcTemplate.query(sqlQuery, this::makeFilm, id);
        if (films == null || films.isEmpty()) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Film not found");
        }
        return films.get(0);
    }

    @Override
    public void deleteFilm(long id) {
        getById(id);
        String sqlQuery = "DELETE FROM films WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        int mpaId = rs.getInt("mpa_id");
        String mpaName = "SELECT mpa_id, name FROM mpa WHERE mpa_id = ?";
        Mpa mpa = jdbcTemplate.query(mpaName, FilmDbStorage::makeMpa, mpaId).get(0);
        Film film = new Film();
        film.setId(rs.getLong("film_id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setDuration(rs.getInt("duration"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setMpa(mpa);
        return film;
    }

    public static Mpa makeMpa(ResultSet rs, int rowNum) throws SQLException {
        return new Mpa(rs.getInt("mpa_id"),
                rs.getString("name")
        );
    }

    private void addGenres(Film film) {
        for (Genre genre : film.getGenres()) {
            genreDBStorage.addFilmGenre(film.getId(), genre.getId());
        }

    }

}
