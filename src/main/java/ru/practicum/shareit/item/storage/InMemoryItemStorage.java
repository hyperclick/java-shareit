package ru.practicum.shareit.item.storage;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Data
@Slf4j
@Component
public class InMemoryItemStorage {
    private Map<Integer, Item> items = new HashMap<>();

    private int lastId = 1;

    public Collection<Item> getAllItems() {
        log.info("items {}", items.toString().toUpperCase());
        return items.values();
    }

    public Item getById(int id) {
        if (!items.containsKey(id)) {
            throw new NoSuchElementException();
        }
        log.info("item {}", items.get(id).toString().toUpperCase());
        return items.get(id);
    }

    public List<Item> getBySearch(String str) {
        str = str.trim().toUpperCase();
        var result = new ArrayList<Item>();
        if (str.length() == 0) {
            return result;
        }
        ItemDto s = null;
        for (Item i : items.values()) {
            if (i.getAvailable() == false) {
                continue;
            }
            if (i.getName() != null) {
                if (i.getName().toUpperCase().contains(str)) {
                    result.add(i);
                    continue;
                }
            }
            if (i.getDescription() != null && i.getDescription().toUpperCase().contains(str)) {
                result.add(i);
            }
        }
        return result;
    }

    public Item addItem(ItemDto itemDto, User owner) {
        var item = ItemMapper.toItem(itemDto, owner);
        validate(item);
        item.setId(lastId++);
        items.put(item.getId(), item);
        log.info("item {} has been added", item.toString().toUpperCase());
        return item;
    }


    public void updateItem(int itemId, ItemDto itemDto, User owner) {
        var item = ItemMapper.toItem(itemDto, owner);
        item.setId(itemId);
        var oldItem = getById(itemId);

        if (oldItem.getOwner().getId() != owner.getId()) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "it is not your item");
        }
        item = item.fillEmpty(oldItem);
        validate(item);
        items.put(item.getId(), item);
        log.info("item {} has been updated", item.toString().toUpperCase());
    }

    private void validate(Item item) {
//        if (item.getOwner() == null) {
//            throw new ValidationException(HttpStatus.INTERNAL_SERVER_ERROR, "Item has no owner");
//        }
        if (item.getAvailable() == null) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Item isn't available");
        }
        if (item.getName() == null || item.getName().equals("")) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Item has no name");
        }
        if (item.getDescription() == null || item.getDescription().equals("")) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Item has no description");
        }
    }

}
