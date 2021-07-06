package com.co.ledger.model;

import lombok.Data;

@Data
public class LoanApplication {

    private String id;
    private LedgerType ledgerType;
    private String bankName;
    private String borrowerName;
    private Double principalAmount;
    private Integer noOfYears;
    private Double rateOfInterest;
}
