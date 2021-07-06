package com.co.ledger.dto;

import lombok.Data;

@Data
public class BalanceResponseDto {
    private String bankName;
    private String borrowerName;
    private Double amountPaid;
    private Integer noOfEmisLeft;

}
