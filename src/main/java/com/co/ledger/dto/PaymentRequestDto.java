package com.co.ledger.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PaymentRequestDto {

    private String id;
    private String loanId;
    private String ledgerType;
    private String bankName;
    private String borrowerName;
    private Double lumpSumAmount;
    private Integer emiNo;

    public void setId() {
        this.id = UUID.randomUUID().toString();
    }
}
