package com.hotelbooking.airbnb.service.impl;

import com.hotelbooking.airbnb.dto.HotelDto;
import com.hotelbooking.airbnb.entity.Hotel;
import com.hotelbooking.airbnb.repository.HotelRepository;
import com.hotelbooking.airbnb.service.HotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {
    private final HotelRepository hotelRepository;

    @Override
    public Hotel createNewHotel(HotelDto hotelDto) {
        return null;
    }

    @Override
    public Hotel getHotelById(Long id) {
        return null;
    }
}
