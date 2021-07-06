package com.co.ledger.repository;

import com.co.ledger.dto.BalanceRequestDto;
import com.co.ledger.dto.BalanceResponseDto;
import com.co.ledger.dto.LoanApplicationRequestDto;
import com.co.ledger.dto.PaymentRequestDto;

public interface LoanApplicationRepository {

    String initiateLoan(LoanApplicationRequestDto loanApplicationRequestDto);

    String makePayment(PaymentRequestDto paymentRequestDto);

    BalanceResponseDto getBalance(BalanceRequestDto balanceRequestDto);
}
