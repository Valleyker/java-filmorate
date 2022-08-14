package ru.yandex.practicum.filmorate.service.genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDBStorage;

import java.util.Collection;

@Service
public class GenreService {

    private final GenreDBStorage genreDBStorage;

    @Autowired
    public GenreService(GenreDBStorage genreDBStorage) {
        this.genreDBStorage = genreDBStorage;
    }

    public Collection<Genre> findAll() {
        return genreDBStorage.findAll();
    }

    public Genre getGenreById(int id) {
        return genreDBStorage.getGenreById(id);
    }

    public Genre updateGenre(Genre genre) {
        return genre;
    }
}
