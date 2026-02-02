package com.hotelbooking.airbnb.service;

import com.hotelbooking.airbnb.dto.HotelPriceDto;
import com.hotelbooking.airbnb.dto.HotelSearchRequest;
import com.hotelbooking.airbnb.entity.Room;
import lombok.NonNull;
import org.springframework.data.domain.Page;

public interface InventoryService {
    void initializeRoomForYear(Room room);

    void deleteAllInventories(Room room);

    Page<@NonNull HotelPriceDto> searchHotels(HotelSearchRequest hotelSearchRequest);
}
