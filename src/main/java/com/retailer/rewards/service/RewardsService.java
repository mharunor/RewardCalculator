package com.retailer.rewards.service;

import com.retailer.rewards.dto.CustomerRewardsResponse;
import com.retailer.rewards.dto.MonthlyPoints;
import com.retailer.rewards.model.Transaction;
import com.retailer.rewards.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class RewardsService {

    private static final DateTimeFormatter MONTH_KEY_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM");

    private final TransactionRepository transactionRepository;
    private final RewardsCalculatorService calculatorService;

    public RewardsService(TransactionRepository transactionRepository,
                          RewardsCalculatorService calculatorService) {
        this.transactionRepository = transactionRepository;
        this.calculatorService = calculatorService;
    }

    /**
     * Builds a rewards summary (per month + total) for every customer that
     * has at least one recorded transaction.
     */
    public List<CustomerRewardsResponse> getAllCustomerRewards() {
        Map<String, List<Transaction>> byCustomer = transactionRepository.findAll().stream()
                .collect(Collectors.groupingBy(Transaction::getCustomerId));

        return byCustomer.keySet().stream()
                .map(this::buildCustomerRewards)
                .sorted(Comparator.comparing(CustomerRewardsResponse::getCustomerId))
                .toList();
    }

    /**
     * Builds a rewards summary for a single customer, or returns null if the
     * customer has no recorded transactions.
     */
    public CustomerRewardsResponse getCustomerRewards(String customerId) {
        return buildCustomerRewards(customerId);
    }

    private CustomerRewardsResponse buildCustomerRewards(String customerId) {
        List<Transaction> transactions = transactionRepository.findByCustomerId(customerId);

        if (transactions.isEmpty()) {
            return null;
        }

        String customerName = transactions.get(0).getCustomerName();

        // TreeMap keeps months in chronological order in the response
        Map<String, Long> monthlyTotals = new TreeMap<>();
        long total = 0L;

        for (Transaction t : transactions) {
            long points = calculatorService.calculatePoints(t.getAmount());
            String monthKey = t.getTransactionDate().format(MONTH_KEY_FORMAT);
            monthlyTotals.merge(monthKey, points, Long::sum);
            total += points;
        }

        List<MonthlyPoints> monthlyPoints = monthlyTotals.entrySet().stream()
                .map(e -> new MonthlyPoints(formatMonthLabel(e.getKey()), e.getValue()))
                .toList();

        return new CustomerRewardsResponse(customerId, customerName, monthlyPoints, total);
    }

    private String formatMonthLabel(String monthKey) {
        YearMonth ym = YearMonth.parse(monthKey, MONTH_KEY_FORMAT);
        String monthName = ym.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        return monthName + " " + ym.getYear();
    }
}
