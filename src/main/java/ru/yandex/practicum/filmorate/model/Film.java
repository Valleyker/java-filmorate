package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class Film {
    private String name;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    private int duration;
    private int id;
}
