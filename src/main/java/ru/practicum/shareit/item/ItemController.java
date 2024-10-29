package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentInfoDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.services.ItemService;

import java.util.Collection;

import static ru.practicum.shareit.constans.Constants.USER_PARM_HEADER;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService service;

    @GetMapping("/{id}")
    public ItemDto get(@RequestHeader(USER_PARM_HEADER) long userId, @PathVariable long id) {
        log.info("==>Получение Item по id: {}", id);
        ItemDto itemDto = service.findById(id, userId);
        return itemDto;
    }

    @GetMapping
    public Collection<ItemDto> getByOwnerId(@RequestHeader(USER_PARM_HEADER) long userId) {
        log.info("==>Получение Item по Владельцу: {}", userId);
        Collection<ItemDto> itemsByOwner = service.findByOwner(userId);
        return itemsByOwner;
    }

    @GetMapping("/search")
    public Collection<ItemDto> getItemBySearch(@RequestParam String text) {
        log.info("==>Получение Item по поиску со словом : {}", text);
        Collection<ItemDto> itemsBySearch = service.findBySearch(text);
        return itemsBySearch;
    }

    @PostMapping
    public ItemDto create(@RequestHeader(USER_PARM_HEADER) long userId,
                          @RequestBody @Valid ItemDto itemDto) {
        log.info("==>Создание Item: {} с владельцем {}", itemDto, userId);
        ItemDto newItemDto = service.create(itemDto, userId);
        return newItemDto;
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(USER_PARM_HEADER) long userId,
                          @PathVariable long itemId,
                          @RequestBody ItemDto itemDto) {
        log.info("==>Обновление Item: {} владельца {}", itemDto, userId);
        itemDto.setId(itemId);
        ItemDto updItemDto = service.update(itemDto, userId);
        return updItemDto;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        log.info("==>Удаление Item по: {}", id);
        service.delete(id);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@NotNull @RequestHeader(USER_PARM_HEADER) long userId,
                                    @PathVariable long itemId,
                                    @RequestBody @Valid CommentInfoDto commentInfoDto) {
        log.info("==>Создание коментария к Item по: {}", itemId);
        CommentDto commentDto = service.createComment(itemId, userId, commentInfoDto);
        return commentDto;
    }
}