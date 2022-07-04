package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.storage.InMemoryItemStorage;
import ru.practicum.shareit.storage.InMemoryUserStorage;

import java.util.Collection;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/items")
@Slf4j
@AllArgsConstructor
public class ItemController2 { //почему без 2 не запускается?
    InMemoryItemStorage inMemoryItemStorage;
    InMemoryUserStorage inMemoryUserStorage;

    @GetMapping
    private Collection<ItemDto> getAllItems() {
        return inMemoryItemStorage.getAllItems();
    }

    @GetMapping("/{id}")
    private ItemDto getItem(@PathVariable("id") int id) {
        try {
            return inMemoryItemStorage.getById(id);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

//    @GetMapping("/{owner}/{id}")
//    private Item getItemOfUser(@PathVariable("owner") User owner, @PathVariable("id") int id) {
//        try {
//            var item = inMemoryItemStorage.getById(id);
////            var user = inMemoryUserStorage.getUsers();
//            return owner.stream().map(userStorage::getById).collect(Collectors.toSet());
//        } catch (NoSuchElementException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
//        }
//    }

    @GetMapping("/{str}")//как замапить этот метод?
    private ItemDto searchItem(@PathVariable String str) {
        return inMemoryItemStorage.getBySearch(str);
    }

    @PostMapping
    private ItemDto addItem(@RequestBody ItemDto item) {
        try {
            inMemoryItemStorage.addItem(item);
            return item;
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping
    private ItemDto updateItem(@RequestBody ItemDto item) {
        try {
            inMemoryItemStorage.updateItem(item);
            return item;
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
