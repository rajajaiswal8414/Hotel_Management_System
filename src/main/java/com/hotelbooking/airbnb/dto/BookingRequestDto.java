package com.hotelbooking.airbnb.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingRequestDto {
    @NotNull
    private Long hotelId;
    @NotNull
    private Long roomId;

    @NotNull
    @FutureOrPresent(message = "Check-in date must be today or future")
    private LocalDate checkInDate;

    @NotNull
    @Future(message = "Check-out date must be in the future")
    private LocalDate checkOutDate;

    @NotNull
    private Integer roomsCount;
}
