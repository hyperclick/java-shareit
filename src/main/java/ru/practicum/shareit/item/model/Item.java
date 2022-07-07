package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.user.model.User;

@Data
@AllArgsConstructor
public class Item {
    private int id;
    private String name;
    private String description;
    private Boolean available;
    private User owner;
    private ItemRequest request;

    public Item fillEmpty(Item i) {
        if (id == 0) {
            id = i.id;
        }
        if (getName() == null) {
            name = i.name;
        }
        if (getDescription() == null) {
            description = i.description;
        }
        if (getAvailable() == null) {
            available = i.available;
        }
        if (getOwner() == null) {
            owner = i.owner;
        }
        if (getRequest() == null) {
            request = i.request;
        }
        return this;
    }
}
