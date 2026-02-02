package com.hotelbooking.airbnb.controller;

import com.hotelbooking.airbnb.dto.BookingResponseDto;
import com.hotelbooking.airbnb.dto.BookingRequestDto;
import com.hotelbooking.airbnb.dto.GuestDto;
import com.hotelbooking.airbnb.service.BookingService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class HotelBookingController {
    private final BookingService bookingService;

    @PostMapping("/init")
    public ResponseEntity<@NonNull BookingResponseDto> initializeBooking(@RequestBody BookingRequestDto bookingRequestDto){
        return ResponseEntity.ok(bookingService.initializeBooking(bookingRequestDto));
    }

    @PostMapping("/{bookingId}/add-guests")
    public ResponseEntity<@NonNull BookingResponseDto> addGuests(@PathVariable Long bookingId, @RequestBody List<GuestDto> guestDtoList){
        return ResponseEntity.ok(bookingService.addGuests(bookingId, guestDtoList));
    }
}
