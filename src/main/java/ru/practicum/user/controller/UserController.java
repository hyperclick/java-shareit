package ru.practicum.user.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.exception.ValidationException;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.UserMapper;
import ru.practicum.user.storage.UserRepository;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
@Slf4j
@AllArgsConstructor
public class UserController {
    //    private InMemoryUserStorage inMemoryUserStorage;
    private final UserRepository userRepository;

    @GetMapping
    private Collection<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    private UserDto getUser(@PathVariable("id") int id) {
        if (!userRepository.existsById(id)) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "");
        }
        return UserMapper.toUserDto(userRepository.getReferenceById(id));
    }

    @PostMapping
    private UserDto addUser(@RequestBody UserDto user) {
        validate(user);
        var s = userRepository.saveAndFlush(UserMapper.toUser(user));
        return UserMapper.toUserDto(s);
    }

    private void validate(UserDto user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "email cannot be empty");
        }
        if (!user.getEmail().contains("@")) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "email should contain @");
        }
    }

    @PatchMapping("/{id}")
    private UserDto updateUser(@RequestBody UserDto user, @PathVariable int id) {
        try {
            var old = userRepository.getReferenceById(id);
            var u = UserMapper.toUser(user);
            u.setId(id);
            u.fillEmpty(old);
            userRepository.saveAndFlush(u);
            return UserMapper.toUserDto(userRepository.getReferenceById(id));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    private void deleteUser(@PathVariable int id) {
        try {
            userRepository.deleteById(id);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
