package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class BaseItemRepository implements  ItemRepository {
    final HashMap<Long, Item> items;

    @Override
    public Item create(Item item) {
        item.setId(getId());
        items.put(item.getId(), item);
        Item itemNew = items.get(item.getId());
        return itemNew;
    }

    @Override
    public Item update(long itemId, Item updItem) {
        Item oldItem = items.get(itemId);

        if (updItem.getDescription() != null && !updItem.getDescription().equals(oldItem.getDescription())) {
            oldItem.setDescription(updItem.getDescription());
        }
        if (updItem.getName() != null && !updItem.getName().equals(oldItem.getName())) {
            oldItem.setName(updItem.getName());
        }
        if (updItem.getAvailable() != null) {
            oldItem.setAvailable(updItem.getAvailable());
        }
        items.put(itemId, oldItem);
        return items.get(itemId);
    }

    @Override
    public void delete(long id) {
        items.remove(id);
    }

    @Override
    public Optional<Item> findById(long id) {
        Item item = items.get(id);
        return Optional.ofNullable(item);
    }

    @Override
    public Collection<Item> findByOwner(long ownerId) {
        Collection<Item> listItem = items.values()
                .stream()
                .filter(item -> item.getOwnerId() == ownerId)
                .toList();
        return listItem;

    }

    @Override
    public Collection<Item> findBySearch(String text) {
        String textToLower = text.toLowerCase();
        Collection<Item> listItem = new ArrayList<>();
        if (!text.isBlank()) {
            listItem = items.values().stream()
                    .filter(item -> item.getAvailable() == true)
                    .filter(item -> item.getName().toLowerCase().contains(textToLower)
                            || item.getDescription().toLowerCase().contains(textToLower))
                    .toList();
        }
        return listItem;
    }

    private long getId() {
        long lastId = items.values().stream()
                .mapToLong(Item::getId)
                .max()
                .orElse(0);
        return lastId + 1;
    }
}