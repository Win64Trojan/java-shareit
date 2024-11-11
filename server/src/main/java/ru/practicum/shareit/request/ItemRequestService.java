package ru.practicum.shareit.request;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfoDto;


import java.util.List;

public interface ItemRequestService {
    List<ItemRequestInfoDto> findAllByUserId(Long userId);

    ItemRequestInfoDto create(ItemRequestDto itemRequestRequestDto);

    ItemRequestInfoDto findItemRequestById(Long itemRequestId, Long userId);

    List<ItemRequestInfoDto> findAllUsersItemRequest();
}