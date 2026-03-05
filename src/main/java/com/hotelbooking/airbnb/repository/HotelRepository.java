package com.hotelbooking.airbnb.repository;

import com.hotelbooking.airbnb.entity.Hotel;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<@NonNull Hotel, @NonNull Long> {
    @Query("SELECT h FROM Hotel h WHERE h.active = true OR h.owner.id = :userId")
    List<Hotel> findAllActiveOrOwnedBy(@Param("userId") Long userId);
}
