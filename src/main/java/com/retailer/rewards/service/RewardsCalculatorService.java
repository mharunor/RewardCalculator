package com.retailer.rewards.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Applies the retailer's reward point rules to a single transaction amount:
 *
 *   - 2 points for every dollar spent over $100
 *   - 1 point for every dollar spent between $50 and $100
 *
 * Example: a $120 purchase = 2x$20 (over $100) + 1x$50 (the $50-$100 band) = 90 points.
 */
@Service
public class RewardsCalculatorService {

    private static final BigDecimal TIER1_THRESHOLD = BigDecimal.valueOf(50);
    private static final BigDecimal TIER2_THRESHOLD = BigDecimal.valueOf(100);
    private static final BigDecimal TIER2_MULTIPLIER = BigDecimal.valueOf(2);

    public long calculatePoints(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return 0L;
        }

        // Dollars that fall in the $50-$100 band (capped at $100, floored at $50)
        BigDecimal tier1Amount = amount.min(TIER2_THRESHOLD).subtract(TIER1_THRESHOLD).max(BigDecimal.ZERO);

        // Dollars that exceed $100
        BigDecimal tier2Amount = amount.subtract(TIER2_THRESHOLD).max(BigDecimal.ZERO);

        BigDecimal points = tier1Amount.add(tier2Amount.multiply(TIER2_MULTIPLIER));

        return points.setScale(0, RoundingMode.HALF_UP).longValueExact();
    }
}
