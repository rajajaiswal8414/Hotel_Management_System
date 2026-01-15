package com.hotelbooking.airbnb.strategy;

import com.hotelbooking.airbnb.entity.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

public class HolidayPricingStrategy extends PricingDecorator{

    public HolidayPricingStrategy(PricingStrategy wrapped){
        super(wrapped);
    }

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal price = wrapped.calculatePrice(inventory);
        boolean isTodayHoliday = true;
        if(isTodayHoliday){
            price = price.multiply(BigDecimal.valueOf(1.25));
        }
        return price;
    }
}
