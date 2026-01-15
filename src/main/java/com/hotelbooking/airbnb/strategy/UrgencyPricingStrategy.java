package com.hotelbooking.airbnb.strategy;

import com.hotelbooking.airbnb.entity.Inventory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class UrgencyPricingStrategy extends PricingDecorator{

    public UrgencyPricingStrategy(PricingStrategy wrapped){
        super(wrapped);
    }

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal price = wrapped.calculatePrice(inventory);

        long daysRemaining = ChronoUnit.DAYS.between(LocalDate.now(), inventory.getDate());

        if (daysRemaining >= 0 && daysRemaining < 7) {
            price = price.multiply(BigDecimal.valueOf(1.15));
        }
        return price;
    }
}
