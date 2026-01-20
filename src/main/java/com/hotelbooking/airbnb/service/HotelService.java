package com.hotelbooking.airbnb.service;

import com.hotelbooking.airbnb.dto.HotelDto;
import com.hotelbooking.airbnb.dto.HotelInfoDto;
import com.hotelbooking.airbnb.dto.HotelSearchRequest;
import lombok.NonNull;
import org.springframework.data.domain.Page;

import java.util.List;

public interface HotelService {
    HotelDto createNewHotel(HotelDto hotelDto);

    HotelDto getHotelById(Long id);

    HotelDto updateHotelById(Long id, HotelDto hotelDto);

    void deleteHotelById(Long id);

    void activateHotel(Long hotelId);

    List<HotelDto> getAllHotels();


    Page<@NonNull HotelDto> searchHotels(HotelSearchRequest hotelSearchRequest);


    HotelInfoDto getHotelInfoById(Long hotelId);
}
