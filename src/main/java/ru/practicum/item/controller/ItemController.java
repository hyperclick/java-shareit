package ru.practicum.item.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.booking.BookingService;
import ru.practicum.exception.ValidationException;
import ru.practicum.item.CommentMapper;
import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.model.Comment;
import ru.practicum.item.service.CommentsService;
import ru.practicum.item.ItemMapper;
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
    private final CommentsService commentsService;
    private final BookingService bookingService;
    private final ItemMapper itemMapper;

    @GetMapping
    private Collection<ItemDto> getAllUserItems(@RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.getAllUserItems(userId);
    }

    @GetMapping("/{id}")
    private ItemDto getItem(@PathVariable("id") int id, @RequestHeader("X-Sharer-User-Id") int userId) {
        var item = itemService.getById(id);
        var dto = itemMapper.toItemDto(item);
        if (userId != item.getOwner().getId()) {
            dto.setLastBooking(null);
            dto.setNextBooking(null);
        }
        return dto;
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
            return itemMapper.toItemDto(itemService.update(itemId, itemDto, userRepository.getReferenceById(userId)));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{itemId}/comment")
    private CommentDto addComment(@PathVariable int itemId, @RequestBody CommentDto dto, @RequestHeader("X-Sharer-User-Id") int authorId) {
        var model = CommentMapper.toModel(dto, itemService.getById(itemId), userRepository.getReferenceById(authorId));
        validate(model);
        return CommentMapper.toDto(commentsService.addNew(itemService.getById(itemId), dto, authorId));

    }

    private void validate(Comment model) {
        if (model.getText() == null || model.getText().trim().length() == 0) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "");
        }
//        var bookings = bookingService.getAllStartedByBookerAndItem(model.getAuthor().getId(), model.getItem().getId(), Status.APPROVED);
        var bookings = bookingService.getAllStartedByBookerAndItem(model.getAuthor().getId(), model.getItem().getId(), true, false);
        if (bookings.size() == 0) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "");
        }
    }
}
