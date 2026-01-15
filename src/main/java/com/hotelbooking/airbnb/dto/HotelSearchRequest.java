package com.hotelbooking.airbnb.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class HotelSearchRequest {

    @NotBlank
    private String city;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    @Min(1)
    private Integer roomsCount;

    private Integer pageNumber = 0;
    private Integer pageSize = 10;
}