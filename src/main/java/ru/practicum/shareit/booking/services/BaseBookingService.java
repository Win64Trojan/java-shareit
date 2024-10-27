package ru.practicum.shareit.booking.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingApproveDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.OutputBookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidatetionConflict;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BaseBookingService implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private static final Sort NEWEST_FIRST = Sort.by(Sort.Direction.DESC, "start");

    @Override
    @Transactional
    public void delete(long id) {
        Booking booking = getBooking(id);
        bookingRepository.delete(booking);
    }

    @Override
    @Transactional
    public OutputBookingDto create(BookingDto bookingDto, long bookerId) {
        User booker = getUser(bookerId);
        Item item = getItem(bookingDto.getItemId());

        if (!item.getAvailable()) {
            throw new ValidationException("Item недоступен");
        }
        if (bookingRepository.isAvailable(item.getId(), bookingDto.getStart(), bookingDto.getEnd())) {
            throw new ValidationException("Item на эти даты недоступен");
        }
        if (item.getOwner().getId().equals(bookerId)) {
            throw new ValidationException("Владелец не может создать booking");
        }
        bookingDto.setBooker(booker.getId());

        Booking booking = Booking.builder()
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .item(item)
                .booker(booker)
                .status(BookingStatus.WAITING)
                .build();
        return BookingMapper.toOutputBookingDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public OutputBookingDto approve(BookingApproveDto bookingApproveDto, long id) {
        Booking booking = getBooking(bookingApproveDto.getId());
        Item item = booking.getItem();

        if (!item.getOwner().getId().equals(id)) {
            throw new ValidationException("Подтвердить Booking может только владелец");
        }
        if (booking.getStatus().equals(BookingStatus.WAITING)) {
            if (bookingApproveDto.getApproved()) {
                booking.setStatus(BookingStatus.APPROVED);
            } else {
                booking.setStatus(BookingStatus.REJECTED);
            }
        } else {
            throw new ValidationException("Booking в статусе {}" + booking.getStatus());
        }
        return BookingMapper.toOutputBookingDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional(readOnly = true)
    public OutputBookingDto findById(long bookingId, long userId) {
        Booking booking = getBooking(bookingId);
        OutputBookingDto bookingDto;
        Item item = getItem(booking.getItem().getId());
        if (item.getOwner().getId().equals(userId) || booking.getBooker().getId().equals(userId)) {
            bookingDto = BookingMapper.toOutputBookingDto(booking);
        } else {
            throw new ValidationException("Booking  доступен только Owner и Booker");
        }
        return bookingDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OutputBookingDto> findByBookerId(long bookerId, String status) {
        List<Booking> listBooking = new ArrayList<>();
        User booker = getUser(bookerId);
        if (BookingStatus.from(status) == null) {
            throw new ValidatetionConflict("Некорректный статус Booking");
        }
        BookingStatus bookingStatus = BookingStatus.from(status);
        switch (bookingStatus) {
            case ALL:
                listBooking = bookingRepository.findByBooker(booker, NEWEST_FIRST);
                break;
            case CURRENT:
                listBooking = bookingRepository.findByBookerAndEndAfterAndStartBefore(booker, LocalDateTime.now(), LocalDateTime.now(), NEWEST_FIRST);
                break;
            case FUTURE:
                listBooking = bookingRepository.findByBookerAndStartAfter(booker, LocalDateTime.now(), NEWEST_FIRST);
                break;
            case PAST:
                listBooking = bookingRepository.findByBookerAndEndBefore(booker, LocalDateTime.now(), NEWEST_FIRST);
                break;
            case WAITING:
            case REJECTED:
            case APPROVED:
                listBooking = bookingRepository.findByBookerAndStatusEquals(booker, status, NEWEST_FIRST);
                break;

        }
        return listBooking.stream()
                .map(BookingMapper::toOutputBookingDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OutputBookingDto> findByOwnerId(long ownerId, String status) {
        List<Booking> listBooking = new ArrayList<>();
        User owner = getUser(ownerId);
        if (BookingStatus.from(status) == null) {
            throw new ValidatetionConflict("Некорректный статус Booking");
        }
        BookingStatus bookingStatus = BookingStatus.from(status);
        switch (bookingStatus) {
            case ALL:
                listBooking = bookingRepository.findByItemOwner(owner, NEWEST_FIRST);
                break;
            case CURRENT:
                listBooking = bookingRepository.findByItemOwnerAndEndAfterAndStartBefore(owner, LocalDateTime.now(), LocalDateTime.now(), NEWEST_FIRST);
                break;
            case FUTURE:
                listBooking = bookingRepository.findByItemOwnerAndStartAfter(owner, LocalDateTime.now(), NEWEST_FIRST);
                break;
            case PAST:
                listBooking = bookingRepository.findByItemOwnerAndEndBefore(owner, LocalDateTime.now(), NEWEST_FIRST);
                break;
            case WAITING:
            case REJECTED:
            case APPROVED:
                listBooking = bookingRepository.findByItemOwnerAndStatusEquals(owner, status, NEWEST_FIRST);
                break;
        }
        return listBooking.stream()
                .map(BookingMapper::toOutputBookingDto)
                .toList();
    }

    private User getUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: не найден:" + userId));

    }

    private Item getItem(long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item  с id: не найден: " + itemId));
    }

    private Booking getBooking(long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking с id: не найден: " + bookingId));
    }
}
