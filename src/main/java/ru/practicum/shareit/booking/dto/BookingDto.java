package ru.practicum.shareit.booking.dto;


import lombok.AllArgsConstructor;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
public class BookingDto {
    UUID id;
    LocalDate start;
    LocalDate end;
    Item item;
    User booker;
    Status status;

    public static class Item extends ru.practicum.shareit.item.model.Item {
//        UUID id;
//        String name;

        public Item(int id, String name, String description, boolean available, User owner, ItemRequest request) {
            super(id, name, description, available, owner, request);
        }
    }
}
