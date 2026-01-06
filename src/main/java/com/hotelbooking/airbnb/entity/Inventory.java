package com.hotelbooking.airbnb.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(uniqueConstraints = @UniqueConstraint(name = "unique_hotel_room_date",columnNames = {"hotel_id", "room_id", "date"}))
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", updatable = false)
    private Hotel hotel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", updatable = false)
    private Room room;

    @Column(updatable = false)
    private LocalDate date;

    @Column(updatable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer bookedCount;

    @Column(updatable = false)
    private Integer totalCount;

    @Column(updatable = false, precision = 5, scale = 2)
    private BigDecimal surgeFactor;

    @Column(updatable = false, precision = 10, scale = 2)
    private BigDecimal price; // basePrice * surgeFactor

    @Column(updatable = false)
    private String city;

    @Column(updatable = false)
    private boolean closed;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
