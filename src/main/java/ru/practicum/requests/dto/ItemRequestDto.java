package ru.practicum.requests.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ItemRequestDto {
    private Long id;
    String description;
    @JsonProperty("requester_id")
    Integer requesterId;
    LocalDate created;
}
