package com.hotelbooking.airbnb.dto;

import com.hotelbooking.airbnb.entity.*;
import com.hotelbooking.airbnb.entity.enums.BookingStatus;
import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

@Data
public class BookingDto {

    private Long id;

    private Long hotelId;
    private String hotelName;

    private Long roomId;
    private String roomType;

    private Long userId;

    private Integer roomsCount;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    private Long paymentId;
    private BookingStatus bookingStatus;

    private Set<GuestDto> guests;
}