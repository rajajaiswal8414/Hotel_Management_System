package com.hotelbooking.airbnb.strategy;

import com.hotelbooking.airbnb.entity.Inventory;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public abstract class PricingDecorator implements PricingStrategy {
    protected final PricingStrategy wrapped;
}
