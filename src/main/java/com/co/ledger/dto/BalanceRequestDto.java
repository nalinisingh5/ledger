package com.co.ledger.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class BalanceRequestDto {

    private String id;
    private String loanId;
    private String ledgerType;
    private String bankName;
    private String borrowerName;
    private Integer emiNo;

    public void setId() {
        this.id = UUID.randomUUID().toString();
    }
}
