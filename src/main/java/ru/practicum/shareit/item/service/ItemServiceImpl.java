package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.storage.InMemoryItemStorage;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemServiceImpl implements ItemService {

    private final InMemoryItemStorage inMemoryItemStorage;

    public ItemServiceImpl(InMemoryItemStorage inMemoryItemStorage) {
        this.inMemoryItemStorage = inMemoryItemStorage;
    }

    @Override
    public Collection<ItemDto> getAllUserItems(int userId) {
        return inMemoryItemStorage
                .getAllItems()
                .stream()
                .filter(i -> i.getOwner().getId() == userId)
                .map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public ItemDto getById(int id) {
        return ItemMapper.toItemDto(inMemoryItemStorage.getById(id));
    }

    @Override
    public List<ItemDto> searchAvailable(String text) {
        return inMemoryItemStorage.getBySearch(text).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public ItemDto addItem(ItemDto itemDto, User owner) {
        return ItemMapper.toItemDto(inMemoryItemStorage.addItem(itemDto, owner));
    }

    @Override
    public ItemDto update(int itemId, ItemDto itemDto, User owner) {
        inMemoryItemStorage.updateItem(itemId, itemDto, owner);
        return getById(itemId);
    }
}
