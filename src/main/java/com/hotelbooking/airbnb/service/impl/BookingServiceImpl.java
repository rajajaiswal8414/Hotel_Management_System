package com.hotelbooking.airbnb.service.impl;

import com.hotelbooking.airbnb.dto.BookingDto;
import com.hotelbooking.airbnb.dto.BookingRequestDto;
import com.hotelbooking.airbnb.dto.GuestDto;
import com.hotelbooking.airbnb.entity.*;
import com.hotelbooking.airbnb.entity.enums.BookingStatus;
import com.hotelbooking.airbnb.exception.APIException;
import com.hotelbooking.airbnb.exception.ResourceNotFoundException;
import com.hotelbooking.airbnb.repository.*;
import com.hotelbooking.airbnb.service.BookingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final GuestRepository guestRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public BookingDto initializeBooking(BookingRequestDto bookingRequestDto) {
        log.info("Initialising booking for hotel : {}, room: {}, date {}-{}", bookingRequestDto.getHotelId(),
                bookingRequestDto.getRoomId(), bookingRequestDto.getCheckInDate(),
                bookingRequestDto.getCheckOutDate());

        Hotel hotel = hotelRepository.findById(bookingRequestDto.getHotelId())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel", "hotelId", bookingRequestDto.getHotelId()));

        Room room = roomRepository.findById(bookingRequestDto.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room", "roomId", bookingRequestDto.getRoomId()));

        List<Inventory> inventoryList = inventoryRepository.findAndLockAvailableInventory(room.getId(),
                bookingRequestDto.getCheckInDate(), bookingRequestDto.getCheckOutDate(), bookingRequestDto.getRoomsCount());

        long daysCount = ChronoUnit.DAYS.between(bookingRequestDto.getCheckInDate(), bookingRequestDto.getCheckOutDate());

        if(inventoryList.size() != daysCount){
            throw new APIException("Room is not available anymore");
        }

        // Reserved the room / update the booked count of inventories

        for(Inventory inventory : inventoryList){
            inventory.setReservedCount(inventory.getReservedCount() + bookingRequestDto.getRoomsCount());
        }
        inventoryRepository.saveAll(inventoryList);



        //TODO: Dynamic Price Model
        BigDecimal amount = room.getBasePrice()
                .multiply(BigDecimal.valueOf(daysCount))
                .multiply(BigDecimal.valueOf(bookingRequestDto.getRoomsCount()));

        //Create booking
        Booking booking = Booking.builder()
                .bookingStatus(BookingStatus.RESERVED)
                .hotel(hotel)
                .room(room)
                .checkInDate(bookingRequestDto.getCheckInDate())
                .checkOutDate(bookingRequestDto.getCheckOutDate())
                .roomsCount(bookingRequestDto.getRoomsCount())
                .user(getCurrentUser())
                .amount(amount)
                .build();

        booking = bookingRepository.save(booking);
        return modelMapper.map(booking, BookingDto.class);
    }

    @Override
    @Transactional
    public BookingDto addGuests(Long bookingId, List<GuestDto> guestDtoList) {
        log.info("Adding guests for booking with ID: {}", bookingId);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "bookingId", bookingId));

        if(hasBookingExpired(booking)){
            throw new APIException("Booking has already expired");
        }

        if(booking.getBookingStatus() != BookingStatus.RESERVED){
            throw new APIException("Booking is not under reserved state, cannot add guests");
        }

        List<Guest> guests = guestDtoList.stream()
                        .map(guestDto -> {
                            Guest guest = modelMapper.map(guestDto, Guest.class);
                            guest.setUser(getCurrentUser());
                            guest.setBooking(booking);
                            return guest;
                        }).toList();

        booking.getGuests().addAll(guests);
        booking.setBookingStatus(BookingStatus.GUESTS_ADDED);
        bookingRepository.save(booking);

        return modelMapper.map(booking, BookingDto.class);
    }

    public boolean hasBookingExpired(Booking booking){
        return booking.getCreatedAt().plusMinutes(10).isBefore(LocalDateTime.now());
    }

    public User getCurrentUser(){
        User user = new User();
        user.setId(1L);
        return user;
    }
}
