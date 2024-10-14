package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDate;

@Data
public class BookingDto {
    final LocalDate start;
    final LocalDate end;
    final long item;
    final BookingStatus status;
}