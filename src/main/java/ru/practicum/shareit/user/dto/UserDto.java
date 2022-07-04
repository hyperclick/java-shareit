package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class UserDto {
    int id;
    String name;
    String email;
}
