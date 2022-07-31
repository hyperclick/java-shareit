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
    private LocalDateTime startDateTime;
    @JsonProperty("end")
    private LocalDateTime endDateTime;
    private ItemDto item;
    private int itemId;
    private UserDto booker;
    private Status status;
}
