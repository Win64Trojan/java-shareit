package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jdk.jfr.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;

@Data
@AllArgsConstructor
public class Item {
    private Long id;
    private Long ownerId;
    @NotBlank(message = "Наименование должен быть указано")
    private String name;
    @NotBlank(message = "Описание должен быть указано")
    private String description;
    @BooleanFlag
    @NotNull(message = "Доступность не может быть null")
    private Boolean available;
    private ItemRequest request;
}