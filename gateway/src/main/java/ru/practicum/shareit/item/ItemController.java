package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemClient client;
    static final String userParmHeader = "X-Sharer-User-Id";

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@RequestHeader(userParmHeader) long userId, @PathVariable long id) {
        log.info("Get item {}, userId={}", id, userId);
        return client.getItem(id, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getByOwnerId(@RequestHeader(userParmHeader) Long userId) {
        log.info("Get getByOwnerId ={}", userId);
        return client.getByOwnerId(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItemBySearch(@RequestParam String text) {
        log.info("==>getItemBySearch : {}", text);

        return client.findBySearch(text);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(userParmHeader) long userId,
                                         @RequestBody @Valid ItemDto itemDto) {
        log.info("==>create Item: {} с владельцем {}", itemDto, userId);
        return client.create(userId,itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader(userParmHeader) long userId,
                                         @PathVariable long itemId,
                                         @RequestBody ItemDto itemDto) {
        log.info("==>update Item: {} владельца {}", itemDto, userId);
        itemDto.setId(itemId);
        return client.update(itemDto,userId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        log.info("==>Delete Item по: {}", id);
        client.delete(id);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader(userParmHeader) long userId,
                                                @PathVariable long itemId,
                                                @RequestBody CommentRequestDto commentRequestDto) {
        log.info("==>Create comment to Item id: {}", itemId);
        return client.addComment(userId, itemId, commentRequestDto);

    }
}