package com.hotelbooking.airbnb.service.impl;

import com.hotelbooking.airbnb.dto.*;
import com.hotelbooking.airbnb.entity.Hotel;
import com.hotelbooking.airbnb.entity.Room;
import com.hotelbooking.airbnb.entity.User;
import com.hotelbooking.airbnb.exception.APIException;
import com.hotelbooking.airbnb.exception.ResourceNotFoundException;
import com.hotelbooking.airbnb.repository.HotelRepository;
import com.hotelbooking.airbnb.repository.InventoryRepository;
import com.hotelbooking.airbnb.repository.RoomRepository;
import com.hotelbooking.airbnb.repository.UserRepository;
import com.hotelbooking.airbnb.security.util.AuthUtil;
import com.hotelbooking.airbnb.service.HotelService;
import com.hotelbooking.airbnb.service.InventoryService;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {
    private final RoomRepository roomRepository;
    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;
    private final InventoryService inventoryService;
    private final AuthUtil authUtil;

    @Override
    public HotelDto createNewHotel(HotelDto hotelDto) {
        log.info("Creating a new hotel with name: {}", hotelDto.getName());
        boolean isManager = authUtil.getPrincipal().getAuthorities().stream()
                .anyMatch(a -> Objects.equals(a.getAuthority(), "ROLE_HOTEL_MANAGER"));

        if (!isManager) {
            throw new APIException(HttpStatus.FORBIDDEN, "Only Hotel Managers can create hotels");
        }
        Hotel hotel = modelMapper.map(hotelDto, Hotel.class);
        hotel.setActive(false);

        User currentUser = userRepository.getReferenceById(authUtil.loggedInUserId());
        hotel.setOwner(currentUser);

        hotel = hotelRepository.save(hotel);
        log.info("Created a new hotel with ID: {}", hotel.getId());
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public List<HotelDto> getAllHotels(){
        List<Hotel> hotels = hotelRepository.findAll();

        return hotels.stream()
                .map(hotel -> modelMapper.map(hotel, HotelDto.class))
                .toList();
    }

    @Override
    public HotelInfoDto getHotelInfoById(Long hotelId) {
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel", "hotelId", hotelId));

        List<RoomDto> rooms = hotel.getRooms()
                .stream().map((element) -> modelMapper.map(element, RoomDto.class))
                .toList();
        return new HotelInfoDto(modelMapper.map(hotel, HotelDto.class), rooms);
    }

    @Override
    public HotelDto getHotelById(Long id) {
        log.info("Getting the hotel with ID: {}", id);
        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel", "hotelId", id));

        validateHotelOwnership(hotel);

        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    @Transactional
    public HotelDto updateHotelById(Long id, HotelDto hotelDto) {
        log.info("Updating the hotel with ID: {}", id);
        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel", "hotelId", id));

        validateHotelOwnership(hotel);

        hotel.setCity(hotelDto.getCity());
        hotel.setName(hotelDto.getName());
        hotel.setAmenities(hotelDto.getAmenities());
        hotel.setPhotos(hotelDto.getPhotos());
        hotel.setActive(hotelDto.getActive());
        hotel.setContactInfo(hotelDto.getContactInfo());
        hotelRepository.save(hotel);
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    @Transactional
    public void deleteHotelById(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel", "hotelId", hotelId));

        validateHotelOwnership(hotel);

        for (Room room : hotel.getRooms()) {
            inventoryService.deleteAllInventories(room);
            roomRepository.delete(room);
        }

         hotelRepository.delete(hotel);
    }

    @Override
    @Transactional
    public void activateHotel(Long hotelId) {
        log.info("Activating the hotel with ID: {}", hotelId);
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel", "hotelId", hotelId));
        hotel.setActive(true);

        log.info("Activating the hotel: {}", hotel.getActive());

        for(Room room : hotel.getRooms()){
            if (!inventoryRepository.existsByRoom(room)) {
                inventoryService.initializeRoomForYear(room);
            }
        }
    }

    private void validateHotelOwnership(Hotel hotel) {
        Long loggedInUserId = authUtil.loggedInUserId();
        if (!hotel.getOwner().getId().equals(loggedInUserId)) {
            throw new APIException(HttpStatus.FORBIDDEN, "Access denied: You are not the owner of this hotel.");
        }
    }
}
