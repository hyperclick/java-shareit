package ru.practicum.booking.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.booking.BookingMapper;
import ru.practicum.booking.BookingService;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.item.ItemMapper;

import java.util.Collection;
import java.util.stream.Collectors;


@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final ItemMapper itemMapper;

    @PostMapping
    private BookingDto createNew(@RequestBody BookingDto dto, @RequestHeader("X-Sharer-User-Id") int bookerId){

        return BookingMapper.toDto( bookingService.createNew( dto,  bookerId ), itemMapper);
    }

    @PatchMapping("/{bookingId}")
    private BookingDto update(@PathVariable int bookingId,  @RequestHeader("X-Sharer-User-Id") int ownerId, Boolean approved){
        return BookingMapper.toDto(bookingService.update(bookingId, ownerId, approved), itemMapper);
    }

    @GetMapping("/{bookingId}")
    private BookingDto get(@PathVariable int bookingId,  @RequestHeader("X-Sharer-User-Id") int userId){
        return BookingMapper.toDto(bookingService.getById(bookingId, userId),itemMapper);
    }
    @GetMapping
    private Collection<BookingDto> get(@RequestHeader("X-Sharer-User-Id") int bookerId){
        var r = bookingService.getAllByBooker(bookerId)
                .stream()
                .map(x->BookingMapper.toDto(x,itemMapper))
                .collect(Collectors.toList());
        return r;
    }
    @GetMapping("/owner")
    private Collection<BookingDto> getByOwner(@RequestHeader("X-Sharer-User-Id") int ownerId){
        var r = bookingService.getAllByOwner(ownerId)
                .stream()
                .map(x->BookingMapper.toDto(x,itemMapper))
                .collect(Collectors.toList());
        return r;
    }
}
