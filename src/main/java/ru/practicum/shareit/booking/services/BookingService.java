package ru.practicum.shareit.booking.services;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingApproveDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.OutputBookingDto;

import java.util.List;

@Service
public interface BookingService {
    OutputBookingDto create(BookingDto bookingDto, long id);

    OutputBookingDto approve(BookingApproveDto bookingApproveDto, long id);

    void delete(long id);

    OutputBookingDto findById(long bookingId, long userId);

    List<OutputBookingDto> findByBookerId(long bookerId, String status);

    List<OutputBookingDto> findByOwnerId(long ownerId, String status);
}
