package com.hotelbooking.airbnb.repository;

import com.hotelbooking.airbnb.entity.Hotel;
import com.hotelbooking.airbnb.entity.Inventory;
import com.hotelbooking.airbnb.entity.Room;
import jakarta.persistence.LockModeType;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<@NonNull Inventory, @NonNull Long> {

    void deleteByRoom(Room room);

    boolean existsByRoom(Room room);

//    @Query("""
//    SELECT
//        i.hotel.id as id,
//        i.hotel.name as name,
//        i.hotel.city as city,
//        i.hotel.photos as photos,
//        i.hotel.amenities as amenities,
//        i.hotel.contactInfo as contactInfo,
//        i.hotel.active as active,
//        MIN(r.basePrice) as minPrice
//    FROM Inventory i
//    JOIN i.room r
//    WHERE i.city = :city
//        AND i.date BETWEEN :startDate AND :endDate
//        AND i.closed = FALSE
//        AND (
//            i.totalCount - i.bookedCount - i.reservedCount >=
//            CASE
//                WHEN :requestedRooms > ( (:totalGuests + r.capacity - 1) / r.capacity )
//                THEN :requestedRooms
//                ELSE ( (:totalGuests + r.capacity - 1) / r.capacity )
//            END
//        )
//    GROUP BY i.hotel.id, i.hotel.name, i.hotel.city, i.hotel.photos,
//             i.hotel.amenities, i.hotel.contactInfo, i.hotel.active
//    HAVING COUNT(DISTINCT i.date) = :numberOfNights
//""")
//    Page<@NonNull HotelPriceProjection> findHotelsWithAvailableInventory(
//            @Param("city") String city,
//            @Param("startDate") LocalDate startDate,
//            @Param("endDate") LocalDate endDate,
//            @Param("requestedRooms") Integer requestedRooms,
//            @Param("totalGuests") Integer totalGuests,
//            @Param("numberOfNights") Long numberOfNights,
//            Pageable pageable
//    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    SELECT i
    FROM Inventory i
    WHERE i.room.id = :roomId
        AND i.date BETWEEN :checkInDate AND :checkOutDate
        AND i.closed = false
        AND (i.totalCount - i.bookedCount - i.reservedCount) >= :roomsCount
""")
    List<Inventory> findAndLockAvailableInventory(
            @Param("roomId") Long roomId,
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate,
            @Param("roomsCount") Integer roomsCount
    );

    List<Inventory> findByHotelAndDateBetween(Hotel hotel, LocalDate startDate, LocalDate endDate);
}
