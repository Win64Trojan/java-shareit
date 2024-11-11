package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfoDto;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/requests")
public class ItemRequestController {
    static final String userParmHeader = "X-Sharer-User-Id";
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestInfoDto create(@RequestHeader(userParmHeader) Long userId,
                                     @RequestBody ItemRequestDto itemRequestDto) {
        itemRequestDto.setRequestorId(userId);
        log.info("Создание request ");
        return itemRequestService.create(itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestInfoDto> findAll(@RequestHeader(userParmHeader) Long userId) {
        log.info("Получение requests ");
        return itemRequestService.findAllByUserId(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestInfoDto findItemRequestById(@RequestHeader(userParmHeader) Long userId,
                                                  @PathVariable(value = "requestId") Long requestId) {
        log.info("Получен requests по {requestId}" + requestId);
        return itemRequestService.findItemRequestById(requestId, userId);
    }

    @GetMapping("/all")
    public List<ItemRequestInfoDto> findAllUsersItemRequest() {
        log.info("Получиение списока запросов, созданных другими пользователями");
        return itemRequestService.findAllUsersItemRequest();
    }

}