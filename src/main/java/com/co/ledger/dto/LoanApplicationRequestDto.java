package com.co.ledger.dto;

import lombok.Data;

@Data
public class LoanApplicationRequestDto {

    private String id;
    private String ledgerType;
    private String bankName;
    private String borrowerName;
    private Double principalAmount;
    private Integer noOfYears;
    private Double rateOfInterest;
}
