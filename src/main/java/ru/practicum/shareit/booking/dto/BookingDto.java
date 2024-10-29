package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;


@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDto {
    Long id;
    @FutureOrPresent
    LocalDateTime start;
    @FutureOrPresent
    LocalDateTime end;
    @NotNull
    Long itemId;
    Long booker;
    BookingStatus status;
}