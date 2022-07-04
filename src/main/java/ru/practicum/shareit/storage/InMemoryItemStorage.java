package ru.practicum.shareit.storage;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Data
@Slf4j
@Component
public class InMemoryItemStorage {
    private Map<Integer, ItemDto> items = new HashMap<>();

    private int lastId = 1;

    public Collection<ItemDto> getAllItems() {
        log.info("items {}", items.toString().toUpperCase());
        return items.values();
    }

    public ItemDto getById(int id) {
        if (!items.containsKey(id)) {
            throw new NoSuchElementException();
        }
        log.info("item {}", items.get(id).toString().toUpperCase());
        return items.get(id);
    }

    public ItemDto getBySearch(String str) {
//        var item = items.values();
//        var a = item.stream().map(Item::toString).collect(Collectors.toSet());/*как пройтись по
//        коллекции с помощью стрима?*/
//        for (String s: a) {
//            if(s.contains(str)) {
//
//            }
//        }
        ItemDto s = null;
        for (ItemDto i : items.values()) {
            if (i.getName().toUpperCase().contains(str.toUpperCase()) || i.getDescription().toUpperCase()
                    .contains(str.toUpperCase())) {
                s = i;
            }
        }
        return s;
    }

    public void addItem(ItemDto item) {
        item.setId(lastId++);
        items.put(item.getId(), item);
        log.info("item {} has been added", item.toString().toUpperCase());
    }

    public void updateItem(ItemDto item) {
        items.put(item.getId(), item);
        log.info("item {} has been updated", item.toString().toUpperCase());
    }

    private void validate(Item item) {
    }

}
