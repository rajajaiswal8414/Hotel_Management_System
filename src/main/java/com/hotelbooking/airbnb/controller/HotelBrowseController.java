package com.hotelbooking.airbnb.controller;


import com.hotelbooking.airbnb.dto.HotelDto;
import com.hotelbooking.airbnb.dto.HotelInfoDto;
import com.hotelbooking.airbnb.dto.HotelPriceProjection;
import com.hotelbooking.airbnb.dto.HotelSearchRequest;
import com.hotelbooking.airbnb.service.HotelService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hotels")
public class HotelBrowseController {

    private final HotelService hotelService;

    @PostMapping("/search")
    public ResponseEntity<@NonNull Page<@NonNull HotelDto>> searchHotels(@Valid @RequestBody HotelSearchRequest hotelSearchRequest){
        Page<@NonNull HotelDto> hotelDto = hotelService.searchHotels(hotelSearchRequest);
        return ResponseEntity.ok(hotelDto);
    }

    @GetMapping("/{hotelId}/info")
    public ResponseEntity<@NonNull HotelInfoDto> getHotelInfo(@PathVariable Long hotelId){
        return ResponseEntity.ok(hotelService.getHotelInfoById(hotelId));
    }

}
