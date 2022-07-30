package ru.practicum.booking;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.model.Booking;
import ru.practicum.booking.strorage.BookingRepository;
import ru.practicum.exception.ValidationException;
import ru.practicum.item.dto.ItemBookingDto;
import ru.practicum.item.storage.ItemRepository;
import ru.practicum.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
//    private final ItemMapper itemMapper;
//    private final CommentsService commentsService;

    public BookingService(BookingRepository bookingRepository, ItemRepository itemRepository, UserRepository userRepository ) {
        this.bookingRepository = bookingRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    public Booking createNew(BookingDto dto, int bookerId) {
        validate(dto, bookerId);
        var item = itemRepository.getReferenceById(dto.getItemId());
        if (item.getOwner().getId() == bookerId) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "");
        }
        if (!item.getAvailable()) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "");
        }
        return
                bookingRepository.saveAndFlush(
                        BookingMapper.toModel(
                                dto,
                                userRepository.getReferenceById(bookerId),
                                item)) ;
    }

    private void validate(BookingDto dto, int bookerId) {
        if (!itemRepository.existsById(dto.getItemId())) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "");
        }
        if (!userRepository.existsById(bookerId)) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "");
        }
        if (dto.getStart_date_time().isBefore(LocalDateTime.now()) || dto.getEnd_date_time().isBefore(LocalDateTime.now())) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "");
        }
        if (dto.getEnd_date_time().isBefore(dto.getStart_date_time())) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "");
        }
    }

    public Booking update(int bookingId, int ownerId, Boolean approved) {
        if (approved == null){
            throw new ValidationException(HttpStatus.BAD_REQUEST, "");
        }
        if (!userRepository.existsById(ownerId)){
            throw new ValidationException(HttpStatus.BAD_REQUEST, "");
        }
        var booking = bookingRepository.getReferenceById(bookingId);
        if (booking.getItem().getOwner().getId() != ownerId) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "");
        }
        if (booking.getApproved()){
            if ( approved ){
                throw new ValidationException(HttpStatus.BAD_REQUEST, "");
            }
        }
        booking.setCanceled(!approved);
        booking.setApproved(approved);
        return bookingRepository.saveAndFlush(booking);
    }

    public Booking getById(int bookingId, int userId) {
        if (!bookingRepository.existsById(bookingId)) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "");
        }
        var booking = bookingRepository.getReferenceById(bookingId);
        if (booking.getBooker().getId() != userId && booking.getItem().getOwner().getId() != userId) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "");
        }
        return  booking;
    }

    public Collection<Booking> getAllByBooker(int bookerId) {
        return bookingRepository
                .findAll()
                .stream()
                .filter(b -> b.getBooker().getId() == bookerId)
//                .filter(Booking::getApproved)
//                .sorted((a,b)->Integer.compare(b.getId(),a.getId()))
                .sorted((a, b) -> b.getStart_date_time().compareTo(a.getStart_date_time()))
                .collect(Collectors.toList());
    }

    public Collection<Booking> getAllByOwner(int ownerId) {

        return bookingRepository
                .findAll()
                .stream()
                .filter(b -> b.getItem().getOwner().getId() == ownerId)
//                .filter(Booking::getApproved)
//                .sorted((a,b)->Integer.compare(b.getId(),a.getId()))
                .sorted((a, b) -> b.getStart_date_time().compareTo(a.getStart_date_time()))
                //.map(b->BookingMapper.toDto(b, itemMapper))
                .collect(Collectors.toList());
    }

    public ItemBookingDto getLastBooking(int itemId) {
        return bookingRepository
                .findAll()
                .stream()
                .filter(b->b.getItem().getId() == itemId)
                .filter(b->b.getEnd_date_time().isBefore(LocalDateTime.now()))
                .max((a, b) -> a.getEnd_date_time().compareTo(b.getEnd_date_time()))
                .map(b->new ItemBookingDto(b.getId(),b.getBooker().getId()))
                .orElse(null);
    }

    public ItemBookingDto getNextBooking(int itemId) {
        return bookingRepository
                .findAll()
                .stream()
                .filter(b->b.getItem().getId() == itemId)
                .filter(b->b.getStart_date_time().isAfter(LocalDateTime.now()))
                .min((a, b) -> a.getStart_date_time().compareTo(b.getStart_date_time()))
                .map(b->new ItemBookingDto(b.getId(),b.getBooker().getId()))
                .orElse(null);
    }

//    public Collection<BookingDto> getAllStartedByBookerAndItem(int bookerId, int itemId, Status status) {
//        return getAllByBooker(bookerId)
//                .stream()
//                .filter(b->b.getItem().getId()==itemId)
//                .filter(b->b.getStatus()==status)
//                .toList();
//    }
    public Collection<Booking> getAllStartedByBookerAndItem(int bookerId, int itemId, boolean approved, boolean cancelled) {
        return getAllByBooker(bookerId)
                .stream()
                .filter(b->b.getItem().getId()==itemId)
                .filter(b->b.getApproved()==approved)
                .filter(b->b.getCanceled()==cancelled)
                .filter(b->b.getStart_date_time().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());
    }
}
