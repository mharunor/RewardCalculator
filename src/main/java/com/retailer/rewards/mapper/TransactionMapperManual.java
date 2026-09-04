package com.retailer.rewards.mapper;

import com.retailer.rewards.dto.TransactionDto;
import com.retailer.rewards.model.Transaction;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Primary
public class TransactionMapperManual implements TransactionMapper {

    @Override
    public TransactionDto toDto(Transaction transaction) {
        if (transaction == null) {
            return null;
        }
        return new TransactionDto(
                transaction.getTransactionId(),
                transaction.getCustomerId(),
                transaction.getCustomerName(),
                transaction.getTransactionDate(),
                transaction.getAmount()
        );
    }

    @Override
    public Transaction toEntity(TransactionDto transactionDto) {
        if (transactionDto == null) {
            return null;
        }
        return new Transaction(
                transactionDto.getTransactionId(),
                transactionDto.getCustomerId(),
                transactionDto.getCustomerName(),
                transactionDto.getTransactionDate(),
                transactionDto.getAmount()
        );
    }

    @Override
    public List<TransactionDto> toDtoList(List<Transaction> transactions) {
        return transactions == null ? List.of() : transactions.stream().map(this::toDto).toList();
    }

    @Override
    public List<Transaction> toEntityList(List<TransactionDto> transactionDtos) {
        return transactionDtos == null ? List.of() : transactionDtos.stream().map(this::toEntity).toList();
    }
}
