package com.hotelbooking.airbnb.repository;

import com.hotelbooking.airbnb.entity.Hotel;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends JpaRepository<@NonNull Hotel, @NonNull Long> {

}
