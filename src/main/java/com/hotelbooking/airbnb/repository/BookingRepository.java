package com.hotelbooking.airbnb.repository;

import com.hotelbooking.airbnb.entity.Booking;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<@NonNull Booking, @NonNull Long> {

}
