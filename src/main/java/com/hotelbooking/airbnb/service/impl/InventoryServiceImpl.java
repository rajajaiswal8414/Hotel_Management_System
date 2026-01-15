package com.hotelbooking.airbnb.service.impl;

import com.hotelbooking.airbnb.entity.Inventory;
import com.hotelbooking.airbnb.entity.Room;
import com.hotelbooking.airbnb.repository.InventoryRepository;
import com.hotelbooking.airbnb.service.InventoryService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
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
}
