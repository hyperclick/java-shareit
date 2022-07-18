package ru.practicum.shareit.item.service;


import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;


public interface ItemService {

    Collection<ItemDto> getAllUserItems(int userId);

    ItemDto getById(int id);

    List<ItemDto> searchAvailable(String text);

    ItemDto addItem(ItemDto itemDto, User owner);

    ItemDto update(int itemId, ItemDto itemDto, User owner);
}
