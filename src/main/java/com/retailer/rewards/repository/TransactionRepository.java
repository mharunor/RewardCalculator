package com.retailer.rewards.repository;

import com.retailer.rewards.model.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple in-memory store for transactions. In a production system this
 * would be replaced by a Spring Data JPA repository backed by a real
 * database, but the interface used by the service layer would stay the same.
 */
@Repository
public class TransactionRepository {

    private final Map<String, Transaction> store = new ConcurrentHashMap<>();

    public Transaction save(Transaction transaction) {
        store.put(transaction.getTransactionId(), transaction);
        return transaction;
    }

    public void saveAll(List<Transaction> transactions) {
        transactions.forEach(this::save);
    }

    public List<Transaction> findAll() {
        return List.copyOf(store.values());
    }

    public List<Transaction> findByCustomerId(String customerId) {
        return store.values().stream()
                .filter(t -> t.getCustomerId().equals(customerId))
                .toList();
    }
}
