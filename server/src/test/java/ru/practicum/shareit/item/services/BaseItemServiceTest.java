package ru.practicum.shareit.item.services;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentInfoDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
class BaseItemServiceTest {
    private final BaseItemService service;
    Long itemId1 = 1L;
    Long userId1 = 1L;
    Long userId2 = 2L;
    ItemDto itemDto1 = new ItemDto(1L, "Спальный мешок", "Спальный мешок для похода", false, null, null, null, null);
    ItemDto itemDto2 = new ItemDto(2L, "Дрель", "Дрель для ремонта", true, null, null, null, null);
    ItemDto itemDto3 = new ItemDto(3L, "Книга Овод", "Книга Овод", true, null, null, null, null);

    @Test
    void createItemTest() {
        ItemDto itemDtoNew = ItemDto.builder()
                .name("Лыжи беговые")
                .description("Лыжи для занятия спортом")
                .available(true)
                .build();
        ItemDto itemDtoCreate = service.create(itemDtoNew, userId1);
        itemDtoNew.setId(5L);
        assertEquals(itemDtoNew, itemDtoCreate);
    }

    @Test
    void updateItemTest() {
        itemDto1.setName("Спальный мешок для похода размер XXL");
        ItemDto itemDtoUpdate = service.update(itemDto1, userId1);
        assertEquals(itemDto1, itemDtoUpdate);
    }

    @Test
    void updateItemIfNotValidOwnerTest() {
        itemDto1.setName("Спальный мешок для похода размер XXL");
        assertThrows(ValidationException.class, () -> {
            service.update(itemDto1, userId2);
        }, "Нет сообщения: Некорректный владелец");
    }

    @Test
    void deleteItemTest() {
        service.delete(itemId1);
        assertThrows(NotFoundException.class, () -> {
            service.findById(itemId1, userId1);
        }, "Сообщения что запись не найдена нет");
    }

    @Test
    void findByIdTest() {
        Long itemId = 1L;
        ItemDto itemDto = service.findById(itemId, userId1);
        assertEquals(itemId, itemDto.getId());
    }

    @Test
    void findByOwnerTest() {
        List<ItemDto> items = service.findByOwner(userId2);
        assertEquals(items.size(), 2);
    }

    @Test
    void createCommentTest() {
        CommentDto commentDto = CommentDto.builder()
                .itemId(itemId1)
                .text("Отличный спальный мешок")
                .authorName("Алена")
                .build();
        CommentInfoDto commentNewDto = CommentInfoDto.builder()
                .text("Отличный спальный мешок")
                .build();
        CommentDto commentCreatedDto = service.createComment(itemId1, userId2, commentNewDto);
        commentDto.setId(commentCreatedDto.getId());
        commentDto.setCreated(commentCreatedDto.getCreated());

        assertEquals(commentCreatedDto, commentDto);
    }

    @Test
    void createCommentIfBookingNotFinishTest() {

        CommentInfoDto commentNewDto = CommentInfoDto.builder()
                .itemId(4L)
                .text("Отличный санки")
                .authorId(3L)
                .build();

        assertThrows(ValidationException.class, () -> {
            service.createComment(4L, 3L, commentNewDto);
        }, "Нет сообщения: Бронирование вещи не завершено");
    }

    @Test
    void createCommentIfBookingNotExistTest() {

        CommentInfoDto commentNewDto = CommentInfoDto.builder()
                .itemId(101L)
                .text("Отличный санки")
                .authorId(3L)
                .build();

        assertThrows(ValidationException.class, () -> {
            service.createComment(4L, 3L, commentNewDto);
        }, "Нет сообщения: Бронирование вещи не найдено");
    }

    @Test
    void findBySearchTest() {
        Collection<ItemDto> listBySearch = service.findBySearch("Дрель");
        assertEquals(listBySearch.size(), 1);
    }
}