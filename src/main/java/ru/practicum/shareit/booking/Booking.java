package ru.practicum.shareit.booking;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class Booking {
    final LocalDate start;
    final LocalDate end;
    final long booker;
    final long item;
    final BookingStatus status;
}
