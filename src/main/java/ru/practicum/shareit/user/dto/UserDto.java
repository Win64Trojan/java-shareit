package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    @NotBlank(message = "Наименование должен быть указано")
    private String name;
    @Email(message = "Email имеет некорректный формат")
    @NotBlank(message = "Наименование должен быть указано")
    private String email;
}