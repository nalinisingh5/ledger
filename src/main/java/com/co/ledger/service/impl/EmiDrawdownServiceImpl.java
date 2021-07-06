package com.co.ledger.service.impl;

import com.co.ledger.dto.BalanceRequestDto;
import com.co.ledger.dto.BalanceResponseDto;
import com.co.ledger.dto.PaymentRequestDto;
import com.co.ledger.model.LoanApplication;
import com.co.ledger.repository.EmiDrawdownRepository;
import com.co.ledger.service.EmiDrawdownService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EmiDrawdownServiceImpl implements EmiDrawdownService {

    @Autowired
    EmiDrawdownRepository emiDrawdownRepository;
    @Override
    public void createEmiDrawdowns(LoanApplication loanApplication) {
        emiDrawdownRepository.createEmiDrawdowns(loanApplication);
    }

    @Override
    public String paymentAllocation(PaymentRequestDto paymentRequestDto, Map<String, LoanApplication> loanApplicationMap) {
        return emiDrawdownRepository.allocatePayments(paymentRequestDto, loanApplicationMap);
    }

    @Override
    public BalanceResponseDto getBalance(BalanceRequestDto balanceRequestDto, LoanApplication loanApplication) {
        return emiDrawdownRepository.getBalance(balanceRequestDto, loanApplication);
    }
}
