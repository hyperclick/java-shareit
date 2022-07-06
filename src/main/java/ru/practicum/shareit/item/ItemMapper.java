package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
    }

//    public static Item toItem(ItemDto item) {
//        return new Item(
//                item.getRequest() != null ? item.getRequest().getId() : null,
//                item.getName(),
//                item.getDescription(),
//                item.isAvailable(),
//                item.getOwner(),
//                item.getRequest()
//        );
//    }

    public static Item toItem(ItemDto itemDto, User owner) {
        return new Item(-1, itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(),
                owner, null);
    }

    public static Item updateItem(Item item, ItemDto itemDto) {
        if (itemDto.getName() == null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() == null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() == null) {
            item.setDescription(itemDto.getDescription());
        }
        return item;
    }

//    public static Item createItem(ItemDto itemDto, int id) {
//        var item = new Item();
//        return item;
//    }
}
