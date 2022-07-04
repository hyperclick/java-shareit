package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static ru.practicum.shareit.item.dto.ItemDto toItemDto(Item item) {
        return new ru.practicum.shareit.item.dto.ItemDto(
                item.getRequest() != null ? item.getRequest().getId() : null,
                item.getName(),
                item.getDescription(),
                item.isAvailable()
        );
    }
}
