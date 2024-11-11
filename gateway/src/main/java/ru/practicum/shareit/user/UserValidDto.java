package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserValidDto {
    Long id;
    @NotBlank(message = "Наименование должен быть указано")
    String name;
    @Email(message = "Email имеет некорректный формат")
    @NotBlank(message = "Email должен быть указан")
    String email;
}
