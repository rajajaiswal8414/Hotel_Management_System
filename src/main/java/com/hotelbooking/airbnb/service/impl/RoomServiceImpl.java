package com.hotelbooking.airbnb.service.impl;

import com.hotelbooking.airbnb.dto.RoomDto;
import com.hotelbooking.airbnb.entity.Hotel;
import com.hotelbooking.airbnb.entity.Room;
import com.hotelbooking.airbnb.exception.ResourceNotFoundException;
import com.hotelbooking.airbnb.repository.HotelRepository;
import com.hotelbooking.airbnb.repository.InventoryRepository;
import com.hotelbooking.airbnb.repository.RoomRepository;
import com.hotelbooking.airbnb.service.InventoryService;
import com.hotelbooking.airbnb.service.RoomService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService {
    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final InventoryService inventoryService;


    @Override
    @Transactional
    public RoomDto createNewRoom(Long hotelId, RoomDto roomDto) {
        log.info("Creating a new room in hotel with ID: {}", hotelId);
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel", "hotelId", hotelId));
        Room room = modelMapper.map(roomDto, Room.class);
        room.setHotel(hotel);
        roomRepository.save(room);

        if(room.getHotel().isActive() && !inventoryRepository.existsByRoom(room)){
            inventoryService.initializeRoomForYear(room);
        }

        return modelMapper.map(room, RoomDto.class);
    }

    @Override
    public List<RoomDto> getAllRoomsInHotel(Long hotelId) {
        log.info("Getting all rooms in hotel with ID: {}", hotelId);
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel", "hotelId", hotelId));
        List<Room> rooms = roomRepository.findByHotelId(hotelId);
        return rooms
                .stream()
                .map(room -> modelMapper.map(room, RoomDto.class))
                .toList();
    }

    @Override
    public RoomDto getRoomById(Long hotelId, Long roomId) {
        log.info("Fetching room details. Hotel ID: {}, Room ID: {}", hotelId, roomId);

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "roomId", roomId));
        if(!room.getHotel().getId().equals(hotelId)){
            log.error("Security Alert: Attempt to access room {} for wrong hotel {}", roomId, hotelId);
            throw new ResourceNotFoundException("Room", "hotelId", hotelId);
        }
        return modelMapper.map(room, RoomDto.class);
    }

    @Transactional
    @Override
    public void deleteRoomById(Long hotelId, Long roomId) {
        log.info("Attempting to delete room {} from hotel {}", roomId, hotelId);
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "roomId", roomId));
        if (!room.getHotel().getId().equals(hotelId)) {
            log.error("Security Alert: Unauthorized attempt to delete room {} from hotel {}", roomId, hotelId);
            throw new ResourceNotFoundException("Room", "hotelId", hotelId);
        }

        inventoryService.deleteAllInventories(room);

        roomRepository.delete(room);
        log.info("Successfully deleted room {}", roomId);
    }
}
