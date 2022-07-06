package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.storage.InMemoryItemStorage;
import ru.practicum.shareit.storage.InMemoryUserStorage;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@Slf4j
@AllArgsConstructor
public class ItemController {
    InMemoryItemStorage inMemoryItemStorage;
    InMemoryUserStorage inMemoryUserStorage;

    @GetMapping
    private Collection<ItemDto> getAllUserItems(@RequestHeader("X-Sharer-User-Id") int userId) {
        return inMemoryItemStorage
                .getAllItems()
                .stream()
                .filter(i -> i.getOwner().getId() == userId)
                .map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    private ItemDto getItem(@PathVariable("id") int id) {
        try {
            return ItemMapper.toItemDto(inMemoryItemStorage.getById(id));
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

    @GetMapping("/search")
    private List<ItemDto> searchItem(@RequestParam String text) {
        return inMemoryItemStorage.getBySearch(text).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @PostMapping
    private ItemDto addItem(@RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") int id) {
        try {
            return ItemMapper.toItemDto(inMemoryItemStorage.addItem(itemDto, inMemoryUserStorage.getById(id)));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{itemId}")
    private ItemDto updateItem(@PathVariable int itemId, @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") int userId) {
        try {
            inMemoryItemStorage.updateItem(itemId, itemDto, inMemoryUserStorage.getById(userId));
            var item = inMemoryItemStorage.getById(itemId);
            return ItemMapper.toItemDto(item);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
