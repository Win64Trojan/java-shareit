package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.model.Item;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingApproveDto {
    @NotNull
    Long id;
    Item item;
    Boolean approved;
}