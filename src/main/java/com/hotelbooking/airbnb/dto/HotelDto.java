package com.hotelbooking.airbnb.dto;

import com.hotelbooking.airbnb.entity.HotelContactInfo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HotelDto {

    private Long id;

    @NotBlank(message = "Hotel name is required")
    @Size(min = 3, max = 100, message = "Hotel name must be between 3 and 100 characters")
    private String name;

    @NotBlank(message = "City is required")
    private String city;

    @NotNull(message = "Photos cannot be null")
    @Size(min = 1, message = "At least one photo is required")
    private String[] photos;

    @NotNull(message = "Amenities cannot be null")
    @Size(min = 1, message = "At least one amenity is required")
    private String[] amenities;

    @NotNull(message = "Contact information is required")
    private HotelContactInfo contactInfo;

    private Boolean active;

    private Double minPrice;

    public HotelDto(Long id, String name, String city, String[] photos,
                    String[] amenities, HotelContactInfo contactInfo,
                    Boolean active, Double minPrice) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.photos = photos;
        this.amenities = amenities;
        this.contactInfo = contactInfo;
        this.active = active;
        this.minPrice = minPrice;
    }
}
