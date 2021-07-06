package com.co.ledger.service.impl;

import com.co.ledger.dto.BalanceRequestDto;
import com.co.ledger.dto.BalanceResponseDto;
import com.co.ledger.dto.LoanApplicationRequestDto;
import com.co.ledger.dto.PaymentRequestDto;
import com.co.ledger.repository.LoanApplicationRepository;
import com.co.ledger.service.LoanApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanApplicationServiceImpl implements LoanApplicationService {

    @Autowired
    LoanApplicationRepository loanApplicationRepository;

    @Override
    public String initiateLoan(LoanApplicationRequestDto loanApplicationRequestDto) {
        return loanApplicationRepository.initiateLoan(loanApplicationRequestDto);
    }

    @Override
    public String makePayment(PaymentRequestDto paymentRequestDto) {
        return loanApplicationRepository.makePayment(paymentRequestDto);
    }

    @Override
    public BalanceResponseDto getBalance(BalanceRequestDto balanceRequestDto) {
        return loanApplicationRepository.getBalance(balanceRequestDto);
    }
}
