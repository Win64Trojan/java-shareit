package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfoDto;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
class BaseRequestServiceTest {

    Long userRequestor = 3L;
    Long userRequestorNotValid = 2L;
    Long userRequestorNotExist = 101L;
    Long requestId = 1L;
    private final BaseRequestService service;

    ItemRequestInfoDto createRequest(Long userRequestorId) {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .description("спальник для похода")
                .requestorId(userRequestorId)
                .build();

        ItemRequestInfoDto itemRequestCreatedDto = service.create(itemRequestDto);
        return itemRequestCreatedDto;
    }

    @Test
    void createItemRequestTest() {

        ItemRequestInfoDto itemRequestCreatedDto = createRequest(userRequestor);
        ItemRequestInfoDto itemRequestFindDto = service.findItemRequestById(itemRequestCreatedDto.getId(), itemRequestCreatedDto.getRequestor().getId());
        assertEquals(itemRequestCreatedDto, itemRequestFindDto);
    }

    @Test
    void createItemRequestNotValidUserTest() {
        assertThrows(NotFoundException.class, () -> {
            createRequest(userRequestorNotExist);
        }, "Нет сообения что: User не найден.");
    }

    @Test
    void findItemRequestNotFoundTest() {
        assertThrows(NotFoundException.class, () -> {
            service.findItemRequestById(requestId, userRequestorNotValid);
        }, "Нет сообения что: Request не найден.");
    }

    @Test
    void findfindAllByUserIdTest() {
        ItemRequestInfoDto itemRequestCreatedDto = createRequest(userRequestor);
        assertEquals(service.findAllByUserId(userRequestor).size(), 1);
    }

    @Test
    void findAllUsersItemRequestTest() {
        ItemRequestInfoDto itemRequestCreatedDto = createRequest(userRequestor);
        assertEquals(service.findAllUsersItemRequest().size(), 1);
    }
}