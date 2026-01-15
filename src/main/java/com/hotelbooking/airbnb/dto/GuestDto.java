package com.hotelbooking.airbnb.dto;

import com.hotelbooking.airbnb.entity.User;
import com.hotelbooking.airbnb.entity.enums.Gender;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class GuestDto {

    private Long id;
    private User user;
    private String name;
    private Gender gender;
    private Integer age;
}
