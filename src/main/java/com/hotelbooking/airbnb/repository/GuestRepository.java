package com.hotelbooking.airbnb.repository;

import com.hotelbooking.airbnb.entity.Guest;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestRepository extends JpaRepository<@NonNull Guest, @NonNull Long> {
}