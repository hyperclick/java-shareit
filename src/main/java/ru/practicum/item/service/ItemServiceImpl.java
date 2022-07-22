package ru.practicum.item.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.practicum.exception.ValidationException;
import ru.practicum.item.ItemMapper;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.model.Item;
import ru.practicum.item.storage.ItemRepository;
import ru.practicum.user.model.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Collection<ItemDto> getAllUserItems(int userId) {
        return itemRepository
                .findAll()
                .stream()
                .filter(i -> i.getOwner().getId() == userId)
                .map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public ItemDto getById(int id) {
        if (!itemRepository.existsById(id)) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "");
        }
        return ItemMapper.toItemDto(itemRepository.getReferenceById(id));
    }

    @Override
    public List<ItemDto> searchAvailable(String text) {
        if (text.isEmpty()) {
            return List.of();
        }
        return itemRepository.findAll().stream()
                .filter(Item::getAvailable)
                .filter(x -> contains(x.getName().toUpperCase(), text.toUpperCase()) || contains(x.getDescription().toUpperCase(), text.toUpperCase()))
                .map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    private boolean contains(String str, String find) {
        return str != null && str.contains(find);
    }

    @Override
    public ItemDto addItem(ItemDto itemDto, User owner) {
        var item = ItemMapper.toItem(itemDto, owner);
        validate(item);
        return ItemMapper.toItemDto(itemRepository.saveAndFlush(item));
    }

    @Override
    public ItemDto update(int itemId, ItemDto itemDto, User owner) {
        var old = itemRepository.getReferenceById(itemId);
        if (old.getOwner().getId() != owner.getId()) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "");
        }
        itemRepository.saveAndFlush(ItemMapper.copyNotEmpty(old, itemDto));
        return getById(itemId);
    }

    private void validate(Item item) {
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
