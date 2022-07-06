package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    int id;
    String name;
    //    @Email
    String email;

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
