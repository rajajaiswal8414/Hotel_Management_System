package com.hotelbooking.airbnb.service;

import com.hotelbooking.airbnb.entity.Room;

public interface InventoryService {
    void initializeRoomForYear(Room room);

    void deleteAllInventories(Room room);
}
