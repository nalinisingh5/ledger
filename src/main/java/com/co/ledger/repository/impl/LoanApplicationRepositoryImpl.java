package com.co.ledger.repository.impl;

import com.co.ledger.dto.BalanceRequestDto;
import com.co.ledger.dto.BalanceResponseDto;
import com.co.ledger.dto.LoanApplicationRequestDto;
import com.co.ledger.dto.PaymentRequestDto;
import com.co.ledger.model.LedgerType;
import com.co.ledger.model.LoanApplication;
import com.co.ledger.repository.LoanApplicationRepository;
import com.co.ledger.service.EmiDrawdownService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.Map;

@Repository
public class LoanApplicationRepositoryImpl implements LoanApplicationRepository {

    public static Map<String, LoanApplication> loanApplicationMap = new LinkedHashMap<>();
    @Autowired
    EmiDrawdownService emiDrawdownService;

    @Override
    public String initiateLoan(LoanApplicationRequestDto loanApplicationRequestDto) {
        if (loanApplicationRequestDto.getLedgerType().equals(String.valueOf(LedgerType.LOAN))) {
            LoanApplication loanApplication = new LoanApplication();
            loanApplication.setId(loanApplicationRequestDto.getId());
            loanApplication.setBankName(loanApplicationRequestDto.getBankName());
            loanApplication.setBorrowerName(loanApplicationRequestDto.getBorrowerName());
            loanApplication.setLedgerType(LedgerType.valueOf(loanApplicationRequestDto.getLedgerType()));
            loanApplication.setPrincipalAmount(loanApplicationRequestDto.getPrincipalAmount());
            loanApplication.setRateOfInterest(loanApplicationRequestDto.getRateOfInterest());
            loanApplication.setNoOfYears(loanApplicationRequestDto.getNoOfYears());

            // save loanApplication to loanApplicationMap
            saveLoanApplication(loanApplication);
            // Create Emi Drawdowns
            emiDrawdownService.createEmiDrawdowns(loanApplication);

            return "Congratulations! " + loanApplication.getBorrowerName() + "\nYour loan amount of " + loanApplication.getPrincipalAmount() +
                    " is disbursed at yearly rate% of " + loanApplication.getRateOfInterest() +
                    " for a tenure of " + loanApplication.getNoOfYears() + " years.";
        }
        return "You do not have any Active Loans.";
    }

    @Override
    public String makePayment(PaymentRequestDto paymentRequestDto) {
        PaymentRequestDto paymentRequestDto1 = new PaymentRequestDto();
        paymentRequestDto1.setId();
        paymentRequestDto1 = paymentRequestDto;
        return emiDrawdownService.paymentAllocation(paymentRequestDto1, loanApplicationMap);
    }

    @Override
    public BalanceResponseDto getBalance(BalanceRequestDto balanceRequestDto) {
        BalanceResponseDto balanceResponseDto = new BalanceResponseDto();
        LoanApplication loanApplication = null;
        String loanId = balanceRequestDto.getLoanId();
        if (!loanApplicationMap.containsKey(loanId)) {
            return balanceResponseDto;
        } else {
            loanApplication = loanApplicationMap.get(loanId);
        }
        return emiDrawdownService.getBalance(balanceRequestDto, loanApplication);
    }

    private void saveLoanApplication(LoanApplication loanApplication) {
        if (!loanApplicationMap.containsKey(loanApplication.getId())) {
            loanApplicationMap.put(loanApplication.getId(), loanApplication);
        }
    }
}
