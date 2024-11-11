package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.dto.ItemDto;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingApproveDto {
    @NotNull
    Long id;
    ItemDto item;
    Boolean approved;
}
