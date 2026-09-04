package com.retailer.rewards.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RewardsCalculatorServiceTest {

    private final RewardsCalculatorService calculator = new RewardsCalculatorService();

    @Test
    void exampleFromSpec_120Dollars_yields90Points() {
        assertEquals(90L, calculator.calculatePoints(new BigDecimal("120.00")));
    }

    @Test
    void amountUnder50_yieldsZeroPoints() {
        assertEquals(0L, calculator.calculatePoints(new BigDecimal("45.00")));
    }

    @Test
    void amountExactly50_yieldsZeroPoints() {
        assertEquals(0L, calculator.calculatePoints(new BigDecimal("50.00")));
    }

    @Test
    void amountBetween50And100_yieldsOnePointPerDollarOverFifty() {
        assertEquals(49L, calculator.calculatePoints(new BigDecimal("99.00")));
    }

    @Test
    void amountExactly100_yields50Points() {
        assertEquals(50L, calculator.calculatePoints(new BigDecimal("100.00")));
    }

    @Test
    void amountOver100_appliesBothTiers() {
        assertEquals(250L, calculator.calculatePoints(new BigDecimal("200.00")));
    }

    @Test
    void zeroOrNegativeAmount_yieldsZeroPoints() {
        assertEquals(0L, calculator.calculatePoints(BigDecimal.ZERO));
        assertEquals(0L, calculator.calculatePoints(new BigDecimal("-10.00")));
    }
}
