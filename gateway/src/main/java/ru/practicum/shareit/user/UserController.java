package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserClient client;

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable long id) {
        log.info("Get User with id: {}", id);
        return client.getUser(id);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid UserValidDto userDto) {
        log.info("==>create User: {}", userDto);
        return client.create(userDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable long id, @RequestBody UserValidDto userDto) {
        log.info("==>update User: {}", userDto);
        userDto.setId(id);
        return client.update(userDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        log.info("==>Delete User по: {}", id);
        client.delete(id);
    }
}