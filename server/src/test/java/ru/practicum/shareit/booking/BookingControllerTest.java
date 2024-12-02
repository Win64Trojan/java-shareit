package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.OutputBookingDto;
import ru.practicum.shareit.booking.services.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.services.UserService;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@WebMvcTest(controllers = BookingController.class)
@AutoConfigureMockMvc
class BookingControllerTest {
    @MockBean
    BookingService bookingService;
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    public BookingDto getBookingDto() {
        return BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusHours(1))
                .build();
    }

    public OutputBookingDto getOutputBookingDto() {
        User user = new User(1L, "user", "user@mail.ru");
        User owner = new User(2L, "owner", "owner@mail.ru");
        Item item = new Item(1L, owner,"item", "desc", true, null);
        UserDto userDto = new UserDto(1L, "user", "user@mail.ru");
        ItemDto itemDto = new ItemDto(1L, "item", "desc", true,0L,null,null,null);

        return OutputBookingDto.builder()
                .id(1L)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .item(itemDto)
                .booker(userDto)
                .status(BookingStatus.WAITING)
                .build();

    }

    @Test
    void createTest() throws Exception {
        BookingDto bookingDto = getBookingDto();
        OutputBookingDto outputBookingDto = getOutputBookingDto();


        when(bookingService.create(any(),anyLong())).thenReturn(outputBookingDto);

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bookingDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(bookingService).create(bookingDto, 1L);
    }

    @Test
    void approveTest() throws Exception {
        BookingDto bookingDto = getBookingDto();
        OutputBookingDto outputBookingDto = getOutputBookingDto();
        outputBookingDto.setStatus(BookingStatus.APPROVED);
        when(bookingService.approve(any(), anyLong())).thenReturn(outputBookingDto);

        mvc.perform(patch("/bookings/{bookingId}", bookingDto.getId())
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(outputBookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(outputBookingDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(outputBookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.status", is(outputBookingDto.getStatus().toString())));
    }

    @Test
    void findByIdTest() throws Exception {
        BookingDto bookingDto = getBookingDto();
        OutputBookingDto outputBookingDto = getOutputBookingDto();
        when(bookingService.findById(anyLong(), anyLong())).thenReturn(outputBookingDto);

        mvc.perform(get("/bookings/{bookingId}", bookingDto.getId())
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(outputBookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(outputBookingDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(outputBookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.status", is(outputBookingDto.getStatus().toString())));
    }

    @Test
    void findByBookerIdTest() throws Exception {
        BookingDto bookingDto = getBookingDto();
        OutputBookingDto outputBookingDto = getOutputBookingDto();
        when(bookingService.findByBookerId(anyLong(),anyString()))
                .thenReturn(List.of(outputBookingDto));

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(outputBookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].item.id", is(outputBookingDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[0].booker.id", is(outputBookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(outputBookingDto.getStatus().toString())));
    }

    @Test
    void findByOwnerIdTest() throws Exception {
        BookingDto bookingDto = getBookingDto();
        OutputBookingDto outputBookingDto = getOutputBookingDto();
        when(bookingService.findByOwnerId(anyLong(),anyString()))
                .thenReturn(List.of(outputBookingDto));

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(outputBookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].item.id", is(outputBookingDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[0].booker.id", is(outputBookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(outputBookingDto.getStatus().toString())));
    }
}