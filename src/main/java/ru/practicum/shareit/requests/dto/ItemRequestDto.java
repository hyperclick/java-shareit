package ru.practicum.shareit.requests.dto;

import lombok.AllArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
public class ItemRequestDto {
    int id;
    String description;
    User requester;
    LocalDate created;

    public static class User extends ru.practicum.shareit.user.model.User {
//        UUID id;
//        String name;
//        String email;

        public User(int id, String name, String email) {
            super(id, name, email);
        }
    }
}
