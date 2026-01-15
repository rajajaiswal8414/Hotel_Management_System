package com.hotelbooking.airbnb.controller;

import com.hotelbooking.airbnb.dto.RoomDto;
import com.hotelbooking.airbnb.service.RoomService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/hotels/{hotelId}/rooms")
public class RoomController {
    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<@NonNull RoomDto> createNewRoom(@PathVariable Long hotelId, @RequestBody RoomDto roomDto){
        RoomDto roomDTO = roomService.createNewRoom(hotelId, roomDto);
        return new ResponseEntity<>(roomDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<@NonNull List<RoomDto>> getAllRoomsInHotel(@PathVariable Long hotelId){
        List<RoomDto> roomDTOs = roomService.getAllRoomsInHotel(hotelId);
        return ResponseEntity.ok(roomDTOs);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<@NonNull RoomDto> getRoomById(@PathVariable Long hotelId ,@PathVariable Long roomId){
        RoomDto roomDTO = roomService.getRoomById(hotelId, roomId);
        return ResponseEntity.ok(roomDTO);
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<@NonNull Void> deleteRoomById(@PathVariable Long hotelId, @PathVariable Long roomId){
        roomService.deleteRoomById(hotelId, roomId);
        return ResponseEntity.noContent().build();
    }

}
