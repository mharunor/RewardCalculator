package com.retailer.rewards.controller;

import com.retailer.rewards.dto.CustomerRewardsResponse;
import com.retailer.rewards.dto.TransactionDto;
import com.retailer.rewards.mapper.TransactionMapper;
import com.retailer.rewards.repository.TransactionRepository;
import com.retailer.rewards.service.RewardsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rewards")
public class RewardsController {

    private final RewardsService rewardsService;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public RewardsController(RewardsService rewardsService,
                             TransactionRepository transactionRepository,
                             TransactionMapper transactionMapper) {
        this.rewardsService = rewardsService;
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    /**
     * GET /api/rewards/customers
     * Reward points earned per month and in total, for every customer.
     */
    @GetMapping("/customers")
    public ResponseEntity<List<CustomerRewardsResponse>> getAllCustomerRewards() {
        return ResponseEntity.ok(rewardsService.getAllCustomerRewards());
    }

    /**
     * GET /api/rewards/customers/{customerId}
     * Reward points earned per month and in total, for a single customer.
     */
    @GetMapping("/customers/{customerId}")
    public ResponseEntity<CustomerRewardsResponse> getCustomerRewards(@PathVariable String customerId) {
        CustomerRewardsResponse response = rewardsService.getCustomerRewards(customerId);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/rewards/transactions
     * The raw transaction data set backing the calculations, for reference.
     */
    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDto>> getAllTransactions() {
        return ResponseEntity.ok(transactionMapper.toDtoList(transactionRepository.findAll()));
    }
}
