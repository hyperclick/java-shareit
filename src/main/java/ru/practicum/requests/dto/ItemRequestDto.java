package ru.practicum.requests.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ItemRequestDto {
    private Long id;
    String description;
    Integer requester_id;
    LocalDate created;
}
