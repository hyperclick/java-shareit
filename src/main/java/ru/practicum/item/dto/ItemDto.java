package ru.practicum.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ItemDto {
    private int id;
    private String name;
    private String description;
    private Boolean available;
}
