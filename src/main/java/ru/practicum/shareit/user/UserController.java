package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.services.UserService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService service;


    @GetMapping("/{id}")
    public UserDto get(@PathVariable long id) {
        log.info("Запрос User по id: {}", id);
        UserDto userDto = service.findById(id);
        return userDto;
    }

    @PostMapping()
    public UserDto create(@RequestBody @Valid UserDto userDto) {
        log.info("==>Создание User: {}", userDto);
        UserDto newUserDto = service.create(userDto);
        return newUserDto;
    }

    @PatchMapping("/{id}")
    public UserDto update(@PathVariable long id, @RequestBody UserDto userDto) {
        log.info("==>Обновление User: {}", userDto);
        UserDto userUpdDto = service.update(id, userDto);
        return userUpdDto;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        log.info("==>Удалениее User по: {}", id);
        service.delete(id);
    }
}