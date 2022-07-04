package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Booking {
    UUID id;
    LocalDate start;
    LocalDate end;
    Item item;
    User booker;
    Status status;
}
