package com.retailer.rewards.service;

import com.retailer.rewards.dto.CustomerRewardsResponse;
import com.retailer.rewards.dto.MonthlyPoints;
import com.retailer.rewards.model.Transaction;
import com.retailer.rewards.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class RewardsServiceTest {

    private RewardsService rewardsService;
    private TransactionRepository repository;

    @BeforeEach
    void setUp() {
        repository = new TransactionRepository();
        rewardsService = new RewardsService(repository, new RewardsCalculatorService());

        repository.saveAll(List.of(
                new Transaction("T1", "C001", "Alice Smith", LocalDate.parse("2024-01-05"), new BigDecimal("120.00")),
                new Transaction("T2", "C001", "Alice Smith", LocalDate.parse("2024-01-15"), new BigDecimal("45.00")),
                new Transaction("T3", "C001", "Alice Smith", LocalDate.parse("2024-02-03"), new BigDecimal("150.00"))
        ));
    }

    @Test
    void aggregatesPointsByMonthAndTotal() {
        CustomerRewardsResponse response = rewardsService.getCustomerRewards("C001");

        assertEquals("Alice Smith", response.getCustomerName());
        assertEquals(240L, response.getTotalPoints()); // 90 + 0 + 150

        List<MonthlyPoints> monthly = response.getMonthlyPoints();
        assertEquals(2, monthly.size());
        assertEquals("January 2024", monthly.get(0).getMonth());
        assertEquals(90L, monthly.get(0).getPoints());
        assertEquals("February 2024", monthly.get(1).getMonth());
        assertEquals(150L, monthly.get(1).getPoints());
    }

    @Test
    void unknownCustomer_returnsNull() {
        assertNull(rewardsService.getCustomerRewards("UNKNOWN"));
    }
}
