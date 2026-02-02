package com.hotelbooking.airbnb.controller;


import com.hotelbooking.airbnb.dto.*;
import com.hotelbooking.airbnb.service.HotelService;
import com.hotelbooking.airbnb.service.InventoryService;
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
    private final InventoryService inventoryService;

    @PostMapping("/search")
    public ResponseEntity<@NonNull Page<@NonNull HotelPriceDto>> searchHotels(@Valid @RequestBody HotelSearchRequest hotelSearchRequest){
        Page<@NonNull HotelPriceDto> hotelDto = inventoryService.searchHotels(hotelSearchRequest);
        return ResponseEntity.ok(hotelDto);
    }

    @GetMapping("/{hotelId}/info")
    public ResponseEntity<@NonNull HotelInfoDto> getHotelInfo(@PathVariable Long hotelId){
        return ResponseEntity.ok(hotelService.getHotelInfoById(hotelId));
    }

}
