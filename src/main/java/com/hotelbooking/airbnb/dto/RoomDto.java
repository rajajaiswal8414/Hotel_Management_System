package com.hotelbooking.airbnb.dto;


import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RoomDto {

    @NotNull(message = "Room id cannot be null")
    private Long id;

    @NotBlank(message = "Room type is required")
    private String type;

    @NotNull(message = "Base price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Base price must be greater than 0")
    private BigDecimal basePrice;

    @NotNull(message = "Photos array cannot be null")
    @Size(min = 1, message = "At least one photo is required")
    private String[] photos;

    @NotNull(message = "Amenities array cannot be null")
    @Size(min = 1, message = "At least one amenity is required")
    private String[] amenities;

    @NotNull(message = "Total room count is required")
    @Min(value = 1, message = "Total room count must be at least 1")
    private Integer totalCount;

    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    private Integer capacity;
}
