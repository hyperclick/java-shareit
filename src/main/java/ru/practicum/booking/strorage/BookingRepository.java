package ru.practicum.booking.strorage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.booking.model.Booking;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
}
