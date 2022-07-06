package ru.practicum.shareit.user.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.storage.InMemoryUserStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.UserMapper;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
@Slf4j
@AllArgsConstructor
public class UserController {
    InMemoryUserStorage inMemoryUserStorage;

    @GetMapping
    private Collection<UserDto> getAllUsers() {
        return inMemoryUserStorage.getAllUsers().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    private UserDto getUser(@PathVariable("id") int id) {
        try {
            return UserMapper.toUserDto(inMemoryUserStorage.getById(id));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    private UserDto addUser(@RequestBody UserDto user) {
        var s = inMemoryUserStorage.addUser(UserMapper.toUser(user));
        return UserMapper.toUserDto(s);
    }

    @PatchMapping("/{id}")
    private UserDto updateUser(@RequestBody UserDto user, @PathVariable int id) {
        try {
            var u = UserMapper.toUser(user);
            inMemoryUserStorage.updateUser(u, id);
            return UserMapper.toUserDto(inMemoryUserStorage.getById(id));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    private void deleteUser(@PathVariable int id) {
        try {
            inMemoryUserStorage.deleteUser(id);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
