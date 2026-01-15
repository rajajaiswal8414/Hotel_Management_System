package com.hotelbooking.airbnb.strategy;

import com.hotelbooking.airbnb.entity.Inventory;

import java.math.BigDecimal;

public class SurgePricingStrategy extends PricingDecorator {

    public SurgePricingStrategy(PricingStrategy wrapped){
        super(wrapped);
    }

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal price = wrapped.calculatePrice(inventory);
        return price.multiply(inventory.getSurgeFactor());
    }
}
