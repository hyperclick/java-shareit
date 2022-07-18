package ru.practicum.item;

import org.springframework.stereotype.Component;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.model.Item;
import ru.practicum.user.model.User;

@Component
public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
    }

    public static Item toItem(ItemDto itemDto, User owner) {
        return new Item(-1, itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(),
                owner, null);
    }
}
