package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.FutureOrPresent;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.dto.ItemForRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequestInfoDto {
    Long id;
    String description;
    UserDto requestor;
    @FutureOrPresent
    LocalDateTime created;
    List<ItemForRequestDto> items;
}