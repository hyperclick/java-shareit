package ru.practicum.item.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.service.ItemService;
import ru.practicum.user.storage.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/items")
@Slf4j
@AllArgsConstructor
public class ItemController {
    private final UserRepository userRepository;
    private final ItemService itemService;

    @GetMapping
    private Collection<ItemDto> getAllUserItems(@RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.getAllUserItems(userId);
    }

    @GetMapping("/{id}")
    private ItemDto getItem(@PathVariable("id") int id) {

            return itemService.getById(id);
    }

    @GetMapping("/search")
    private List<ItemDto> searchItem(@RequestParam String text) {
        return itemService.searchAvailable(text);
    }

    @PostMapping
    private ItemDto addItem(@RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") int userId) {
        try {
            return itemService.addItem(itemDto, userRepository.getReferenceById(userId));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{itemId}")
    private ItemDto updateItem(@PathVariable int itemId, @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") int userId) {
        try {
            return itemService.update(itemId, itemDto, userRepository.getReferenceById(userId));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
