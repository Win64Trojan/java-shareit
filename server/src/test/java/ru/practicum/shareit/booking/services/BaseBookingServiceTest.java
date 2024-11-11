package ru.practicum.shareit.booking.services;


import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingApproveDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.OutputBookingDto;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidatetionConflict;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;

import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
class BaseBookingServiceTest {
    private final EntityManager em;
    private final BookingService service;
    ItemDto itemDto1 = new ItemDto(1L, "Спальный мешок", "Спальный мешок для похода", false, null, null, null, null);
    ItemDto itemDto2 = new ItemDto(2L, "Дрель", "Дрель для ремонта", true, null, null, null, null);
    ItemDto itemDto3 = new ItemDto(3L, "Книга Овод", "Книга Овод", true, null, null, null, null);
    UserDto userDto1 = new UserDto(1L, "Алексей", "user1@mail.ru");
    UserDto userDto2 = new UserDto(2L, "Алена", "user2@mail.ru");

    OutputBookingDto getOutputBookingDto() {

        OutputBookingDto outputBookingDto = OutputBookingDto.builder()
                .id(1L)
                .booker(userDto1)
                .item(itemDto2)
                .status(BookingStatus.WAITING)
                .build();
        return outputBookingDto;
    }

    @Test
    void findByIfUserIsOwnerTest() {
        OutputBookingDto outputBookingDto = service.findById(1, 1);
        assertEquals(getOutputBookingDto(), outputBookingDto);
    }

    @Test
    void findByIfUserIsBookerTest() {
        OutputBookingDto outputBookingDto = service.findById(1, 2);
        assertEquals(getOutputBookingDto(), outputBookingDto);
    }

    @Test
    void findByIfUserIsNotOwnerAndIsNotBookerTest() {

        assertThrows(ValidationException.class, () -> {
            service.findById(1, 3);
        }, "Нет сообщения: Booking  доступен только Owner и Booker");

    }

    @Test
    void createBookingTest() {
        BookingDto bookingDto = BookingDto.builder()
                .start(LocalDateTime.now())
                .itemId(3L)
                .build();
        OutputBookingDto outputBookingDto = service.create(bookingDto, 1L);
        assertEquals(outputBookingDto.getId(), 5L);
    }

    @Test
    void createBookingIfItemNotAvailableTest() {
        BookingDto bookingDto = BookingDto.builder()
                .start(LocalDateTime.now())
                .itemId(4L)
                .build();

        assertThrows(ValidationException.class, () -> {
            service.create(bookingDto, 1L);
        }, "Нет сообщения: Item недоступен");
    }

    @Test
    void createBookingOwnerTest() {
        BookingDto bookingDto = BookingDto.builder()
                .start(LocalDateTime.now())
                .itemId(3L)
                .build();
        assertThrows(ValidationException.class, () -> {
            service.create(bookingDto, 2L);
        }, "Сообщение что владелец не может создать Booking нет");
    }

    @Test
    void deleteTest() {
        service.delete(1L);
        assertThrows(NotFoundException.class, () -> {
            service.findById(1, 1);
        }, "Сообщения что запись найдена нет");
    }

    @Test
    void approveIf_APPROVED_TrueTest() {
        BookingApproveDto bookingApproveDto = BookingApproveDto.builder()
                .id(1L)
                .item(itemDto2)
                .approved(true)
                .build();
        OutputBookingDto outputBookingDto = service.approve(bookingApproveDto, 2L);
        assertEquals(outputBookingDto.getStatus(), BookingStatus.APPROVED, "Статус не равен Approved");
    }

    @Test
    void approveIf_APPROVED_FalseTest() {
        BookingApproveDto bookingApproveDto = BookingApproveDto.builder()
                .id(1L)
                .item(itemDto2)
                .approved(false)
                .build();
        OutputBookingDto outputBookingDto = service.approve(bookingApproveDto, 2L);
        assertEquals(outputBookingDto.getStatus(), BookingStatus.REJECTED, "Статус не равен REJECTED");
    }

    @Test
    void approveIf_NotWaitingTest() {
        ItemDto itemDto = new ItemDto(1L, "Дрель", "Дрель для ремонта", false, null, null, null, null);

        BookingApproveDto bookingApproveDto = BookingApproveDto.builder()
                .id(2L)
                .item(itemDto)
                .approved(true)
                .build();

        assertThrows(ValidationException.class, () -> {
            service.approve(bookingApproveDto, 1L);
        }, "Нет сообщения что Booking  не в статусе Wating");
    }

    @Test
    void approveIf_NotOwnerTest() {
        BookingApproveDto bookingApproveDto = BookingApproveDto.builder()
                .id(1L)
                .item(itemDto2)
                .approved(false)
                .build();

        assertThrows(ValidationException.class, () -> {
            service.approve(bookingApproveDto, 3L);
        }, "Нет сообщения что Booking подтверждает не ваделец");
    }

    @Test
    void findByBookerIdStausALLTest() {
        List<OutputBookingDto> listBooking = service.findByBookerId(1, "ALL");
        assertEquals(listBooking.size(), 1);
    }

    @Test
    void findByBookerIdStausCURRENTTest() {
        List<OutputBookingDto> listBooking = service.findByBookerId(2, "CURRENT");
        assertEquals(listBooking.size(), 0);
    }

    @Test
    void findByBookerIdStausPASITest() {
        List<OutputBookingDto> listBooking = service.findByBookerId(2, "PAST");
        assertEquals(listBooking.size(), 1);
    }

    @Test
    void findByBookerIdStausFUTURETest() {
        List<OutputBookingDto> listBooking = service.findByBookerId(4, "FUTURE");
        assertEquals(listBooking.size(), 1);
    }

    @Test
    void findByBookerIfStatusNotValidTest() {
        assertThrows(ValidatetionConflict.class, () -> {
            service.findByBookerId(2, "NotValidStutus");
        }, "Нет сообщения: Некорректный статус Booking");
    }

    @Test
    void findByOwnerIfStatusNotValidTest() {
        assertThrows(ValidatetionConflict.class, () -> {
            service.findByOwnerId(1, "NotValidStutus");
        }, "Нет сообщения: Некорректный статус Booking");
    }

    @Test
    void findByOwnerIdStausALLTest() {
        List<OutputBookingDto> listBooking = service.findByOwnerId(1, "ALL");
        assertEquals(listBooking.size(), 2);
    }

    @Test
    void findByOwnerIdStausCURRENTTest() {
        List<OutputBookingDto> listBooking = service.findByOwnerId(1, "CURRENT");
        assertEquals(listBooking.size(), 0);
    }

    @Test
    void findByOwnerIdStausPASTTest() {
        List<OutputBookingDto> listBooking = service.findByOwnerId(1, "PAST");
        assertEquals(listBooking.size(), 1);
    }

    @Test
    void findByOwnerIdStausFUTURETest() {
        List<OutputBookingDto> listBooking = service.findByOwnerId(1, "FUTURE");
        assertEquals(listBooking.size(), 1);
    }

}