package ru.practicum.shareit.item.services;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

@Service
public interface ItemService {
    ItemDto create(ItemDto item, long ownerId);

    ItemDto update(long id, ItemDto item,long userId);

    void delete(long id);

    ItemDto findById(long id);

    Collection<ItemDto> findByOwner(long ownerId);

    Collection<ItemDto> findBySearch(String text);
}