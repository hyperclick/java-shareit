package ru.practicum.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private int id;
    private String name;
    private String email;

    public User fillEmpty(User u) {
        if (id == 0) {
            id = u.id;
        }
        if (name == null) {
            name = u.name;
        }
        if (email == null) {
            email = u.email;
        }
        return this;
    }
}
