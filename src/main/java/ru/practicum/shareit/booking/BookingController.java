package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingApproveDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.OutputBookingDto;
import ru.practicum.shareit.booking.services.BookingService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.services.UserService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;
    private final UserService userService;
    static final String userParmHeader = "X-Sharer-User-Id";

    @PostMapping
    public OutputBookingDto create(@RequestHeader(userParmHeader) long userId,
                                   @RequestBody @Valid BookingDto bookingDto) {
        log.info("==>Создание Booking: ", bookingDto);
        User booker = UserMapper.toUser(userService.findById(userId));
        bookingDto.setBooker(booker.getId());
        OutputBookingDto bookingDtoNew = bookingService.create(bookingDto, userId);
        log.info("<==Создан Booking ");
        return bookingDtoNew;
    }

    @PatchMapping("/{bookingId}")
    public OutputBookingDto approve(@PathVariable(name = "bookingId") long bookingId,
                                    @RequestParam(value = "approved") boolean approved,
                                    @RequestHeader(userParmHeader) long ownerId) {
        log.info("==> Подтверждение  Booking: владельцем :userId", bookingId, ownerId);
        BookingApproveDto bookingApproveDto = BookingApproveDto.builder()
                .id(bookingId)
                .approved(approved)
                .build();
        OutputBookingDto bookingDto = bookingService.approve(bookingApproveDto, ownerId);
        log.info("<== Booking: потвержден/отклонен", bookingId);
        return bookingDto;
    }

    @GetMapping("/{bookingId}")
    public OutputBookingDto getBooking(@PathVariable(name = "bookingId") long bookingId,
                                       @RequestHeader(userParmHeader) long userId) {
        log.info("==> Получение данных о бронировании :bookingId", bookingId);
        OutputBookingDto bookingDto = bookingService.findById(bookingId, userId);
        log.info("<== Получены данных о бронировании :bookingId", bookingId);
        return bookingDto;
    }

    @GetMapping
    public List<OutputBookingDto> getBookings(@RequestParam(value = "state", defaultValue = "ALL") String status,
                                              @RequestHeader(userParmHeader) long bookerId) {
        log.info("==> Получение бронирований пользователя :bookingId", bookerId);
        BookingStatus bookingStatus = BookingStatus.from(status);
        List<OutputBookingDto> listBookingDto = bookingService.findByBookerId(bookerId, status);
        log.info("<== Получены бронирования пользователя :bookingId", bookerId);
        return listBookingDto;
    }

    @GetMapping("/owner")
    public List<OutputBookingDto> getOwnerBookings(@RequestParam(value = "state", defaultValue = "ALL") String status,
                                                   @RequestHeader(userParmHeader) long ownerId) {
        log.info("==>  Получение бронирований по владельцу :ownerid", ownerId);

        List<OutputBookingDto> listBookingDto = bookingService.findByOwnerId(ownerId, status);
        log.info("Получены бронирования владельца :ownerid", ownerId);
        return listBookingDto;
    }
}
