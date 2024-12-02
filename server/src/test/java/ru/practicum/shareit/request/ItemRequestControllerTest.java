package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfoDto;
import ru.practicum.shareit.user.services.UserService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
class ItemRequestControllerTest {
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private ItemRequestService requestService;

    public ItemRequestInfoDto getItemRequestInfpDto() {
        return ItemRequestInfoDto.builder()
                .id(1L)
                .description("TestItemRequestDescription")
                .requestor(null)
                .created(LocalDateTime.now())
                .items(List.of())
                .build();
    }

    public ItemRequestDto getItemRequestDto() {
        return ItemRequestDto.builder()
                .id(1L)
                .description("TestItemRequestDescription")
                .created(LocalDateTime.now())
                .build();
    }

    @Test
    void create() throws Exception {
        ItemRequestDto itemRequestDto = getItemRequestDto();
        ItemRequestInfoDto itemRequestRequestDto = getItemRequestInfpDto();

        Mockito.when(requestService.create(any()))
                .thenReturn(itemRequestRequestDto);
        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 0L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemRequestDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(requestService).create(itemRequestDto);
    }

    @Test
    void findAllByUserIdTest() throws Exception {
        ItemRequestDto itemRequestDto = getItemRequestDto();
        ItemRequestInfoDto itemRequestRequestDto = getItemRequestInfpDto();
        Mockito.when(requestService.findAllByUserId(anyLong()))
                .thenReturn(List.of(itemRequestRequestDto));

        mvc.perform(get("/requests/{requestId}", 1L)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }


    @Test
    void findItemRequestByIdTest() throws Exception {
        ItemRequestDto itemRequestDto = getItemRequestDto();
        ItemRequestInfoDto itemRequestRequestDto = getItemRequestInfpDto();
        Mockito.when(requestService.findItemRequestById(anyLong(), (anyLong())))
                .thenReturn(itemRequestRequestDto);

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void findAllUsersItemRequestTest() throws Exception {
        ItemRequestDto itemRequestDto = getItemRequestDto();
        ItemRequestInfoDto itemRequestRequestDto = getItemRequestInfpDto();
        Mockito.when(requestService.findAllUsersItemRequest())
                .thenReturn(List.of(itemRequestRequestDto));

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void findAll() throws Exception {
        Mockito.when(requestService.findAllByUserId(anyLong())).thenReturn(Collections.emptyList());

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(requestService, times(1)).findAllByUserId(anyLong());
    }
}