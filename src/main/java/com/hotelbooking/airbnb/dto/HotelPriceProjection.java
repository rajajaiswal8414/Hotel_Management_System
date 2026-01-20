package com.hotelbooking.airbnb.dto;

import com.hotelbooking.airbnb.entity.HotelContactInfo;

public interface HotelPriceProjection {
    Long getId();
    String getName();
    String getCity();
    String[] getPhotos();
    String[] getAmenities();
    HotelContactInfo getContactInfo();
    boolean getActive();
    Double getMinPrice();
}
