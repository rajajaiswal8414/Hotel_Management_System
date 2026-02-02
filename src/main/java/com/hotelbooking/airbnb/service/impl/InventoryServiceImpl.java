package com.hotelbooking.airbnb.service.impl;

import com.hotelbooking.airbnb.dto.HotelPriceDto;
import com.hotelbooking.airbnb.dto.HotelSearchRequest;
import com.hotelbooking.airbnb.entity.Inventory;
import com.hotelbooking.airbnb.entity.Room;
import com.hotelbooking.airbnb.repository.HotelMinPriceRepository;
import com.hotelbooking.airbnb.repository.InventoryRepository;
import com.hotelbooking.airbnb.service.InventoryService;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Transactional
@Slf4j
public class InventoryServiceImpl implements InventoryService {
    private final HotelMinPriceRepository hotelMinPriceRepository;

    private final InventoryRepository inventoryRepository;

    public InventoryServiceImpl(InventoryRepository inventoryRepository,
                                HotelMinPriceRepository hotelMinPriceRepository) {
        this.inventoryRepository = inventoryRepository;
        this.hotelMinPriceRepository = hotelMinPriceRepository;
    }

    @Override
    public void initializeRoomForYear(Room room) {
        List<Inventory> inventoryList = new ArrayList<>();
        LocalDate date = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusYears(1);
        while (!date.isAfter(endDate)){
            Inventory inventory = Inventory.builder()
                    .hotel(room.getHotel())
                    .room(room)
                    .bookedCount(0)
                    .reservedCount(0)
                    .city(room.getHotel().getCity())
                    .date(date)
                    .price(room.getBasePrice())
                    .surgeFactor(BigDecimal.ONE)
                    .totalCount(room.getTotalCount())
                    .closed(false)
                    .build();
            inventoryList.add(inventory);
            date = date.plusDays(1);
        }
        inventoryRepository.saveAll(inventoryList);
    }

    @Override
    public void deleteAllInventories(Room room) {
        inventoryRepository.deleteByRoom(room);
    }

    @Override
    public Page<@NonNull HotelPriceDto> searchHotels(HotelSearchRequest hotelSearchRequest) {
        log.info("Searching hotels for {} city, from {} to {}", hotelSearchRequest.getCity(), hotelSearchRequest.getStartDate(), hotelSearchRequest.getEndDate());
        Pageable pageable = PageRequest.of(hotelSearchRequest.getPageNumber(), hotelSearchRequest.getPageSize());
        long dateCount =
                ChronoUnit.DAYS.between(hotelSearchRequest.getStartDate(), hotelSearchRequest.getEndDate());

        // business logic - 90 days
        AtomicReference<Page<@NonNull HotelPriceDto>> hotelPage =
                new AtomicReference<>(hotelMinPriceRepository.findHotelsWithAvailableInventory(
                        hotelSearchRequest.getCity(),
                        hotelSearchRequest.getStartDate(),
                        hotelSearchRequest.getEndDate().minusDays(1),
                        dateCount, pageable));

        return hotelPage.get();
    }
}
