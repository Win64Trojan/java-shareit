package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.FutureOrPresent;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OutputBookingDto {
    Long id;
    @FutureOrPresent
    LocalDateTime start;
    @FutureOrPresent
    LocalDateTime end;
    ItemDto item;
    UserDto booker;
    BookingStatus status;
}