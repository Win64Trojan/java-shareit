package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingDtoTest {
    private final JacksonTester<BookingDto> json;

    @Test
    void testSerialize() throws Exception {
        UserDto userDto = new UserDto(1L, "user", "user@mail.ru");
        ItemDto itemDto = new ItemDto(1L, "item", "desc", true,0L,null,null,null);
        BookingDto bookingDto = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .booker(1L)
                .itemId(1L)
                .status(BookingStatus.APPROVED)
                .build();

        JsonContent<BookingDto> result = json.write(bookingDto);

        assertThat(result).hasJsonPath("$.id")
                .hasJsonPath("$.start")
                .hasJsonPath("$.end")
                .hasJsonPath("$.status")
                .hasJsonPath("$.booker")
                .hasJsonPath("$.itemId");


        assertThat(result).extractingJsonPathNumberValue("$.id")
                .satisfies(id -> assertThat(id.longValue()).isEqualTo(bookingDto.getId()));
        assertThat(result).extractingJsonPathStringValue("$.start")
                .satisfies(created -> assertThat(created).isNotNull());
        assertThat(result).extractingJsonPathStringValue("$.end")
                .satisfies(created -> assertThat(created).isNotNull());
        assertThat(result).extractingJsonPathStringValue("$.status")
                .satisfies(status -> assertThat(status).isEqualTo(bookingDto.getStatus().toString()));
        assertThat(result).extractingJsonPathNumberValue("$.booker")
                .satisfies(booker -> assertThat(booker.longValue()).isEqualTo(bookingDto.getBooker()));
        assertThat(result).extractingJsonPathNumberValue("$.itemId")
                .satisfies(itemId -> assertThat(itemId.longValue()).isEqualTo(bookingDto.getItemId()));

    }
}