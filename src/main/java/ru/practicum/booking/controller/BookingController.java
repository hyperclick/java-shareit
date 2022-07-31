package ru.practicum.booking.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.booking.BookingMapper;
import ru.practicum.booking.BookingService;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.dto.Status;
import ru.practicum.exception.InternalServerErrorException;
import ru.practicum.exception.ValidationException;
import ru.practicum.item.ItemMapper;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;


@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final ItemMapper itemMapper;

    @PostMapping
    private BookingDto createNew(@RequestBody BookingDto dto, @RequestHeader("X-Sharer-User-Id") int bookerId) {

        return BookingMapper.toDto(bookingService.createNew(dto, bookerId), itemMapper);
    }

    @PatchMapping("/{bookingId}")
    private BookingDto update(@PathVariable int bookingId, @RequestHeader("X-Sharer-User-Id") int ownerId, Boolean approved) {
        return BookingMapper.toDto(bookingService.update(bookingId, ownerId, approved), itemMapper);
    }

    @GetMapping("/{bookingId}")
    private BookingDto get(@PathVariable int bookingId, @RequestHeader("X-Sharer-User-Id") int userId) {
        return BookingMapper.toDto(bookingService.getById(bookingId, userId), itemMapper);
    }

    @GetMapping
    private Collection<BookingDto> get(@RequestParam(defaultValue = "ALL", name = "state") String statusString, @RequestHeader("X-Sharer-User-Id") int bookerId) {
        var status = Status.ALL;
        try {
            status = Status.valueOf(statusString);
        } catch (IllegalArgumentException ex) {
            throw new InternalServerErrorException("Unknown state: " + statusString);
        }
        Status finalStatus = status;
        var r = bookingService.getAllByBooker(bookerId)
                .stream()
                .map(x -> BookingMapper.toDto(x, itemMapper))
                .filter(x -> filterByStatus(x, finalStatus))
                .collect(Collectors.toList());
        return r;
    }

    private boolean filterByStatus(BookingDto x, Status status) {
        if (status == Status.ALL) {
            return true;
        }
        if (status == Status.APPROVED || status == Status.REJECTED || status == Status.WAITING) {
            return x.getStatus() == status;
        }

        if (status == Status.PAST) {
            return x.getEndDateTime().isBefore(LocalDateTime.now());
        }
        if (status == Status.FUTURE) {
            return x.getStartDateTime().isAfter(LocalDateTime.now());
        }
        if (status == Status.CURRENT) {
            return x.getEndDateTime().isAfter(LocalDateTime.now()) && x.getStartDateTime().isBefore(LocalDateTime.now());
        }

        throw new ValidationException(HttpStatus.INTERNAL_SERVER_ERROR, "unexpected status");
    }

    @GetMapping("/owner")
    private Collection<BookingDto> getByOwner(@RequestParam(defaultValue = "ALL", name = "state") String statusString, @RequestHeader("X-Sharer-User-Id") int ownerId) {
        var status = Status.ALL;
        try {
            status = Status.valueOf(statusString);
        } catch (IllegalArgumentException ex) {
            throw new InternalServerErrorException("Unknown state: " + statusString);
        }
        Status finalStatus = status;
        var r = bookingService.getAllByOwner(ownerId)
                .stream()
                .map(x -> BookingMapper.toDto(x, itemMapper))
                .filter(x -> filterByStatus(x, finalStatus))
                .collect(Collectors.toList());
        return r;
    }
}
