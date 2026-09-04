package com.retailer.rewards.mapper;

import com.retailer.rewards.dto.TransactionDto;
import com.retailer.rewards.model.Transaction;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    TransactionDto toDto(Transaction transaction);

    Transaction toEntity(TransactionDto transactionDto);

    List<TransactionDto> toDtoList(List<Transaction> transactions);

    List<Transaction> toEntityList(List<TransactionDto> transactionDtos);
}
