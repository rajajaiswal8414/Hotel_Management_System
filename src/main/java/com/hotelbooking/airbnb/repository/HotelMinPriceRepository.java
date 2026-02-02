package com.hotelbooking.airbnb.repository;

import com.hotelbooking.airbnb.dto.HotelPriceDto;
import com.hotelbooking.airbnb.entity.Hotel;
import com.hotelbooking.airbnb.entity.HotelMinPrice;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface HotelMinPriceRepository extends JpaRepository<@NonNull HotelMinPrice, @NonNull Long> {
    @Query("""
    SELECT new com.hotelbooking.airbnb.dto.HotelPriceDto(
        i.hotel,
        AVG(i.price)
    )
    FROM HotelMinPrice i
    WHERE i.hotel.city = :city
      AND i.hotel.active = true
      AND i.date BETWEEN :startDate AND :endDate
    GROUP BY i.hotel
    HAVING COUNT(i.date) = :dateCount
""")
    Page<@NonNull HotelPriceDto> findHotelsWithAvailableInventory(
            @Param("city") String city,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("dateCount") Long dateCount,
            Pageable pageable
    );
    Optional<HotelMinPrice> findByHotelAndDate(Hotel hotel, LocalDate date);
}
