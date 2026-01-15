package com.hotelbooking.airbnb.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RoomDto {

    private Long id;

    @NotBlank(message = "Room type is required")
    @Size(min = 3, max = 50, message = "Room type must be between 3 and 50 characters")
    private String type;

    @NotNull(message = "Base price is required")
    @Positive(message = "Base price must be greater than 0")
    private BigDecimal basePrice;

    @NotEmpty(message = "At least one photo is required")
    private String[] photos;

    @NotEmpty(message = "At least one amenity is required")
    private String[] amenities;

    @NotNull(message = "Total room count is required")
    @Positive(message = "Total room count must be greater than 0")
    private Integer totalCount;

    @NotNull(message = "Capacity is required")
    @Positive(message = "Capacity must be greater than 0")
    private Integer capacity;
}
