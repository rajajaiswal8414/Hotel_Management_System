package com.hotelbooking.airbnb.repository;

import com.hotelbooking.airbnb.entity.Room;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<@NonNull Room, @NonNull Long> {
    List<Room> findByHotelId(Long hotelId);
}
