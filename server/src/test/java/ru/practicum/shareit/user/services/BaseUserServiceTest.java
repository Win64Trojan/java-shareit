package ru.practicum.shareit.user.services;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
class BaseUserServiceTest {
    private final BaseUserService service;
    UserDto userDtoNew = new UserDto(101L, "Ирина", "user101@mail.ru");
    Long userExistInbase = 2L;
    Long qtyExistsUsrUnBase = 4L;

    @Test
    void findByIdTest() {
        UserDto userDtoFind = service.findById(userExistInbase);
        assertEquals(userDtoFind.getId(), userExistInbase);
    }

    @Test
    void userCreateTest() {
        UserDto userDtoCreate = service.create(userDtoNew);
        assertEquals(userDtoCreate, service.findById(userDtoCreate.getId()));
    }

    @Test
    void userUpdateTest() {
        UserDto userDtoCreate = service.create(userDtoNew);
        userDtoCreate.setName("Ольга");
        UserDto userDtoUpd = service.update(userDtoCreate);
        assertEquals(userDtoCreate.getName(), userDtoUpd.getName());

    }

    @Test
    void userUpdateTestOldEmail() {

        UserDto userDtoCreate = service.create(userDtoNew);
        userDtoCreate.setName("Ольга");
        String oldEmail = userDtoCreate.getEmail();
        String newEmail = "newemail@example.com";
        userDtoCreate.setEmail(newEmail);

        UserDto userDtoUpd = service.update(userDtoCreate);

        assertEquals("Ольга", userDtoUpd.getName());
        assertEquals(newEmail, userDtoUpd.getEmail());
        assertEquals(userDtoCreate.getId(), userDtoUpd.getId());
        assertNotEquals(oldEmail, userDtoUpd.getEmail());
    }

    @Test
    void deleteItemTest() {
        service.delete(userExistInbase);
        assertThrows(NotFoundException.class, () -> {
            service.findById(userExistInbase);
        }, "Сообщения что запись не найдена нет");
    }

    @Test
    void getAllTest() {

        assertEquals(service.getAll().size(), qtyExistsUsrUnBase);
    }

}