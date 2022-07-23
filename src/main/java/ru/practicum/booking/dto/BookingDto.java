package ru.practicum.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.booking.model.Status;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookingDto {
    private int id;
    private LocalDate start_date_time;
    private LocalDate end_date_time;
    private int item_id;
    private int booker_id;
    private Status status;
}
