package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ItemDto {
    int id;
    String name;
    String description;
    Boolean available;
}
