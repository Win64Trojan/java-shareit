package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jdk.jfr.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemDto {
    private  Long id;
    @NotBlank(message = "Наименование должен быть указано")
    private  String name;
    @NotBlank(message = "Описание должно быть указано")
    private  String description;
    @BooleanFlag
    @NotNull(message = "Доступность не может быть null")
    private  Boolean available;
    private  Long request;
}