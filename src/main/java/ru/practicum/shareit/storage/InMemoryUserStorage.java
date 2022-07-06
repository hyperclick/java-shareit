package ru.practicum.shareit.storage;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.exception.ValidationException;
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

    public void updateUser(User user, int id) {
        var u = users.get(id);
        user = user.fillEmpty(u);
        validate(user);
        users.put(id, user);
        log.info("users {} has been updated", users.toString().toUpperCase());
    }

    public void deleteUser(int id) throws ResponseStatusException {
        users.remove(id);
        log.info("user {} has been deleted", users.toString().toUpperCase());
    }

    private void validate(User user) {
        if (user.getEmail() == null || !(user.getEmail().contains("@"))) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "The user email must include @, should be without spaces " +
                    "and shouldn't be blank"); //почему имя исключения включает путь?
        }
        if (user.getName() == null || user.getName().contains(" ") || user.getName().equals("")) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "The user name can't be empty or contains spaces");
        }
        for (User u : users.values()) {
            if (u.getId() == user.getId()) {
                continue;
            }
            if (u.getEmail().equals(user.getEmail())) {
                throw new ValidationException(HttpStatus.CONFLICT, "The user email is already exist");
            }
        }
    }
}
