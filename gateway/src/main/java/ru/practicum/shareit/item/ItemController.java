package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemClient client;

    static final String USER_PARM_HEADER = "X-Sharer-User-Id";

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@RequestHeader(USER_PARM_HEADER) long userId, @PathVariable long id) {
        log.info("Get item {}, userId={}", id, userId);
        return client.getItem(id, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getByOwnerId(@RequestHeader(USER_PARM_HEADER) Long userId) {
        log.info("Get getByOwnerId ={}", userId);
        return client.getByOwnerId(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItemBySearch(@RequestParam String text) {
        log.info("==>getItemBySearch : {}", text);

        return client.findBySearch(text);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(USER_PARM_HEADER) long userId,
                                         @RequestBody @Valid ItemDto itemDto) {
        log.info("==>create Item: {} с владельцем {}", itemDto, userId);
        return client.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader(USER_PARM_HEADER) long userId,
                                         @PathVariable long itemId,
                                         @RequestBody ItemDto itemDto) {
        log.info("==>update Item: {} владельца {}", itemDto, userId);
        itemDto.setId(itemId);
        return client.update(itemDto, userId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        log.info("==>Delete Item по: {}", id);
        client.delete(id);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader(USER_PARM_HEADER) long userId,
                                                @PathVariable long itemId,
                                                @RequestBody CommentRequestDto commentRequestDto) {
        log.info("==>Create comment to Item id: {}", itemId);
        return client.addComment(userId, itemId, commentRequestDto);

    }
}