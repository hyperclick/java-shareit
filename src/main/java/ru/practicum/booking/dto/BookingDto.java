package ru.practicum.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.user.dto.UserDto;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookingDto {
    private int id;
    @JsonProperty("start")
    private LocalDateTime start_date_time;
    @JsonProperty("end")
    private LocalDateTime end_date_time;
    private ItemDto item;
    private int itemId;
    private UserDto booker;
    private Status status;
}
