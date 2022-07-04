package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                (BookingDto.Item) booking.getItem(), /* это точно не верно, т.к. пришлось наследовать Item и
                создавать конструктор super, из которого нельзя исключить ненужные в данном случае поля*/
                booking.getBooker(),
                booking.getStatus()
        );
    }
//        public BookingDto toBookingDto() {
//            TypeMap<Booking, BookingDto> propertyMapper = this.mapper.createTypeMap(Booking.class, Booking.class);
//            // add deep mapping to flatten source's Player object into a single field in destination
//            propertyMapper.addMappings(
//                    mapper -> mapper.map(src -> src.getCreator().getName(), GameDTO::setCreator)
//            );
//        }

}
