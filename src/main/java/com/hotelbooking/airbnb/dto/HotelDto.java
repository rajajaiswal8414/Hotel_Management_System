package com.hotelbooking.airbnb.dto;

import com.hotelbooking.airbnb.entity.HotelContactInfo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class HotelDto {

    private Long id;

    @NotBlank(message = "Hotel name is required")
    @Size(min = 3, max = 100, message = "Hotel name must be between 3 and 100 characters")
    private String name;

    @NotBlank(message = "City is required")
    private String city;

    @NotEmpty(message = "At least one photo is required")
    private String[] photos;

    @NotEmpty(message = "At least one amenity is required")
    private String[] amenities;

    @NotNull(message = "Contact information is required")
    private HotelContactInfo contactInfo;

    private boolean active;
}
