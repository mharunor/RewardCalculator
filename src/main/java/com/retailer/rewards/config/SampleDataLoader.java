package com.retailer.rewards.config;

import com.retailer.rewards.model.Transaction;
import com.retailer.rewards.repository.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Populates the in-memory repository with a hand-picked 3-month data set that
 * exercises every branch of the reward rules:
 *   - amounts below $50 (0 points)
 *   - amounts exactly at the $50 and $100 boundaries
 *   - amounts spanning both the $50-$100 and over-$100 tiers
 *   - a customer with a month that has no transactions at all (Carol, Feb)
 *   - a customer with no transactions in the first month (David, Jan)
 */
@Component
public class SampleDataLoader implements CommandLineRunner {

    private final TransactionRepository transactionRepository;

    public SampleDataLoader(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void run(String... args) {
        List<Transaction> sampleTransactions = List.of(
                // Alice Smith (C001) -> Jan 130, Feb 160, Mar 250, Total 540
                tx("T001", "C001", "Alice Smith", "2024-01-05", "120.00"),
                tx("T002", "C001", "Alice Smith", "2024-01-15", "45.00"),
                tx("T003", "C001", "Alice Smith", "2024-01-22", "90.00"),
                tx("T004", "C001", "Alice Smith", "2024-02-03", "150.00"),
                tx("T005", "C001", "Alice Smith", "2024-02-18", "60.00"),
                tx("T006", "C001", "Alice Smith", "2024-03-09", "200.00"),
                tx("T007", "C001", "Alice Smith", "2024-03-27", "30.00"),

                // Bob Jones (C002) -> Jan 99, Feb 350, Mar 60, Total 509
                tx("T008", "C002", "Bob Jones", "2024-01-08", "50.00"),
                tx("T009", "C002", "Bob Jones", "2024-01-19", "99.00"),
                tx("T010", "C002", "Bob Jones", "2024-01-30", "100.00"),
                tx("T011", "C002", "Bob Jones", "2024-02-11", "250.00"),
                tx("T012", "C002", "Bob Jones", "2024-02-14", "40.00"),
                tx("T013", "C002", "Bob Jones", "2024-03-05", "80.00"),
                tx("T014", "C002", "Bob Jones", "2024-03-21", "80.00"),

                // Carol White (C003) -> Jan 850, Feb (no transactions), Mar 10, Total 860
                tx("T015", "C003", "Carol White", "2024-01-12", "500.00"),
                tx("T016", "C003", "Carol White", "2024-03-16", "60.00"),

                // David Lee (C004) -> Jan (no transactions), Feb 110, Mar 220, Total 330
                tx("T017", "C004", "David Lee", "2024-02-08", "130.00"),
                tx("T018", "C004", "David Lee", "2024-03-02", "130.00"),
                tx("T019", "C004", "David Lee", "2024-03-24", "130.00")
        );

        transactionRepository.saveAll(sampleTransactions);
    }

    private Transaction tx(String id, String customerId, String customerName, String date, String amount) {
        return new Transaction(id, customerId, customerName, LocalDate.parse(date), new BigDecimal(amount));
    }
}
