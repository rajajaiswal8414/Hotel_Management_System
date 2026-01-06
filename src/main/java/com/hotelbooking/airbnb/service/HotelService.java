package com.hotelbooking.airbnb.service;

import com.hotelbooking.airbnb.dto.HotelDto;
import com.hotelbooking.airbnb.entity.Hotel;

public interface HotelService {
    Hotel createNewHotel(HotelDto hotelDto);

    Hotel getHotelById(Long id);
}
