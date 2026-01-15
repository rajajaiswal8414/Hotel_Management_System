package com.hotelbooking.airbnb.repository;

import com.hotelbooking.airbnb.entity.Booking;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<@NonNull Booking, @NonNull Long> {

}
