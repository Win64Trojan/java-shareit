package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    static final String USER_PARM_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(USER_PARM_HEADER) Long userId,
                                         @Valid @RequestBody ItemRequestDto itemRequestRequestDto) {
        return itemRequestClient.create(userId, itemRequestRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> findAll(@RequestHeader(USER_PARM_HEADER) Long userId) {
        return itemRequestClient.findAll(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findItemRequestById(@RequestHeader(USER_PARM_HEADER) Long userId,
                                                      @PathVariable Long requestId) {
        return itemRequestClient.findItemRequestById(userId, requestId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAllUsersItemRequest() {
        return itemRequestClient.findAllUsersItemRequest();
    }
}