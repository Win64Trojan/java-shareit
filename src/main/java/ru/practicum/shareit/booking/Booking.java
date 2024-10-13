package ru.practicum.shareit.booking;

import jakarta.validation.constraints.FutureOrPresent;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
@FieldDefaults
public class Booking {
    @FutureOrPresent
    final LocalDate start;
    @FutureOrPresent
    final LocalDate end;
    final long booker;
    final long item;
    final BookingStatus status;
}
