package ru.practicum.booking;

import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.dto.Status;
import ru.practicum.booking.model.Booking;
import ru.practicum.item.ItemMapper;
import ru.practicum.item.model.Item;
import ru.practicum.user.UserMapper;
import ru.practicum.user.model.User;

/**
 * // TODO .
 */
public class BookingMapper {
    public static Booking toModel(BookingDto dto, User booker, Item item) {
        var model = new Booking();
        model.setBooker(booker);
        model.setItem(item);
        model.setApproved(dto.getStatus() == Status.APPROVED);
        model.setCanceled(dto.getStatus() == Status.CANCELED);
        model.setStart_date_time(dto.getStart_date_time());
        model.setEnd_date_time(dto.getEnd_date_time());
        if (dto.getStatus() == Status.REJECTED) {
            model.setApproved(true);
            model.setCanceled(true);
        }
        return model;
    }

    public static BookingDto toDto(Booking booking,  ItemMapper itemMapper) {
        return new BookingDto(
                booking.getId(),
                booking.getStart_date_time(),
                booking.getEnd_date_time(),
                itemMapper.toItemDto(booking.getItem()),
                booking.getItem().getId(),
                UserMapper.toUserDto(booking.getBooker()),
                getStatus(booking));
    }

    private static Status getStatus(Booking model) {
        if (model.getCanceled()) {
            return Status.REJECTED;
        } else if (model.getApproved()) {
            return Status.APPROVED;
        }
//        else if (model.getCanceled()) {
//            return Status.CANCELED;
//        }
        return Status.WAITING;
    }
}
