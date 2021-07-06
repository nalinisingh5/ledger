package com.co.ledger.service;

import com.co.ledger.dto.BalanceRequestDto;
import com.co.ledger.dto.BalanceResponseDto;
import com.co.ledger.dto.PaymentRequestDto;
import com.co.ledger.model.LoanApplication;

import java.util.Map;

public interface EmiDrawdownService {

    void createEmiDrawdowns(LoanApplication loanApplication);

    String paymentAllocation(PaymentRequestDto paymentRequestDto, Map<String, LoanApplication> loanApplicationMap);

    BalanceResponseDto getBalance(BalanceRequestDto balanceRequestDto, LoanApplication loanApplication);
}
