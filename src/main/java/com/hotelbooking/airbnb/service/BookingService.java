package com.hotelbooking.airbnb.service;

import com.hotelbooking.airbnb.dto.BookingResponseDto;
import com.hotelbooking.airbnb.dto.BookingRequestDto;
import com.hotelbooking.airbnb.dto.GuestDto;

import java.util.List;

public interface BookingService {
    BookingResponseDto initializeBooking(BookingRequestDto bookingRequestDto);

    BookingResponseDto addGuests(Long bookingId, List<GuestDto> guestDtoList);
}
