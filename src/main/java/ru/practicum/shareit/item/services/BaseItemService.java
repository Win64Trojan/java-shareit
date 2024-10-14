package ru.practicum.shareit.item.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class BaseItemService implements  ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto create(ItemDto newItemDto, long ownerId) {
        userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь " + ownerId + "не найден"));
        return ItemMapper.toItemDto(itemRepository.create(ItemMapper.toItem(newItemDto,ownerId)));
    }

    @Override
    public ItemDto update(long itemId, ItemDto itemUpd, long ownerId) {
        userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь " + ownerId + "не найден"));
        itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item " + itemId + "не найден"));
        return ItemMapper.toItemDto(itemRepository.update(itemId, ItemMapper.toItem(itemUpd,ownerId)));
    }

    @Override
    public void delete(long itemId) {
        itemRepository.delete(itemId);
    }

    @Override
    public ItemDto findById(long itemId) {
        itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item " + itemId + "не найден"));
        return ItemMapper.toItemDto(itemRepository.findById(itemId).get());
    }

    @Override
    public Collection<ItemDto> findByOwner(long ownerId) {
        userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь " + ownerId + "не найден"));
        return itemRepository.findByOwner(ownerId)
                .stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public Collection<ItemDto> findBySearch(String text) {

        return itemRepository.findBySearch(text)
                .stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }
}