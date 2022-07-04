package ru.practicum.shareit.user.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.storage.InMemoryUserStorage;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(path = "/users")
@Slf4j
@AllArgsConstructor
public class UserController {
    InMemoryUserStorage inMemoryUserStorage;

    @GetMapping
    private Collection<User> getAllUsers() {
        return inMemoryUserStorage.getAllUsers();
    }

    @GetMapping("/{id}")
    private User getUser(@PathVariable("id") int id) {
        try {
            return inMemoryUserStorage.getById(id);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    private User addUser(@RequestBody User user) { /* @RequestBody означает, что значение аргумента нужно взять
    из тела запроса. При этом объект, который пришёл в теле запроса, например, в виде JSON, будет автоматически
     сконвертирован в Java-объект. */
        return inMemoryUserStorage.addUser(user);
    }

    @PutMapping
    private User updateUser(@RequestBody User user) {
        try {
            inMemoryUserStorage.updateUser(user);
            return user;
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    private void deleteUser(@PathVariable int id) { /* С помощью @PathVariable Spring узнаёт, какая часть URL-пути
    будет автоматически подтягиваться в аргумент метода, к которому применена эта аннотация.*/
        try {
            inMemoryUserStorage.deleteUser(id);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
