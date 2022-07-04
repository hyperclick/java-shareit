package ru.practicum.shareit.storage;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Data
@Slf4j
@Component
public class InMemoryUserStorage {

    private Map<Integer, User> users = new HashMap<>();

    private int lastId = 1;


    public Collection<User> getAllUsers() {
//        log.info(users.toString().toUpperCase());
        log.info("users {}", users.toString().toUpperCase());
        return users.values();
    }

    public User getById(int id) {
        if (!users.containsKey(id)) {
            throw new NoSuchElementException();
        }
        return users.get(id);
    }

    public User addUser(User user) {
        validate(user);
        user.setId(lastId++);
        users.put(user.getId(), user);
        log.info("user {} has been added", users.toString().toUpperCase());
        return user;
    }

    public void updateUser(User user) {
//        validate(user);
        for (User u : users.values()) {
            if (u.getEmail().equals(user.getEmail())) { // почему вылезает непонятное исключение?
                throw new ru.practicum.shareit.exception.ValidationException("The user email is already exist");
            }
        }
        users.put(user.getId(), user);/* как обновлять юзера, сохраняя все поля старого, даже если в реквесте этих
         полей нет у нового юзера?*/
        log.info("users {} has been updated", users.toString().toUpperCase());
    }

    public void deleteUser(int id) throws ResponseStatusException {
        users.remove(id);
        log.info("user {} has been deleted", users.toString().toUpperCase());
    }

    private void validate(User user) {
        if (user.getEmail() == null || !(user.getEmail().contains("@"))) {
            throw new ru.practicum.shareit.exception.ValidationException("The user email must include @, should be without spaces " +
                    "and shouldn't be blank"); //почему имя исключения включает путь?
        }
        if (user.getName() == null || user.getName().contains(" ") || user.getName().equals("")) {
            throw new ru.practicum.shareit.exception.ValidationException("The user name can't be empty or contains spaces");
        }
        for (User u : users.values()) {
            if (u.getEmail().equals(user.getEmail())) { // почему вылезает непонятное исключение?
                throw new ru.practicum.shareit.exception.ValidationException("The user email is already exist");
            }
        }
    }
}
