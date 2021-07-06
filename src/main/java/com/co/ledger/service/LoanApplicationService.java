package com.co.ledger.service;


import com.co.ledger.dto.BalanceRequestDto;
import com.co.ledger.dto.BalanceResponseDto;
import com.co.ledger.dto.LoanApplicationRequestDto;
import com.co.ledger.dto.PaymentRequestDto;

public interface LoanApplicationService {
    String initiateLoan(LoanApplicationRequestDto loanApplicationRequestDto);

    String makePayment(PaymentRequestDto paymentRequestDto);

    BalanceResponseDto getBalance(BalanceRequestDto balanceRequestDto);
}
