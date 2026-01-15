package com.hotelbooking.airbnb.service;

import com.hotelbooking.airbnb.dto.BookingDto;
import com.hotelbooking.airbnb.dto.BookingRequestDto;
import com.hotelbooking.airbnb.dto.GuestDto;
import lombok.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface BookingService {
    BookingDto initializeBooking(BookingRequestDto bookingRequestDto);

    BookingDto addGuests(Long bookingId, List<GuestDto> guestDtoList);
}
