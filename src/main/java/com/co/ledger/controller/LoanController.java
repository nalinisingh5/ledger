package com.co.ledger.controller;

import com.co.ledger.dto.BalanceRequestDto;
import com.co.ledger.dto.BalanceResponseDto;
import com.co.ledger.dto.LoanApplicationRequestDto;
import com.co.ledger.dto.PaymentRequestDto;
import com.co.ledger.service.LoanApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
@RequestMapping("/loan")
public class LoanController {

    @Autowired
    LoanApplicationService loanApplicationService;

    @PostMapping("/apply")
    public String applyLoan(@RequestBody LoanApplicationRequestDto loanApplicationRequestDto) {
        return loanApplicationService.initiateLoan(loanApplicationRequestDto);
    }

    @PostMapping("/make/payment")
    public String makePayment(@RequestBody PaymentRequestDto paymentRequestDto) {
        return loanApplicationService.makePayment(paymentRequestDto);
    }

    @PostMapping("/get/balance")
    public BalanceResponseDto getBalance(@RequestBody BalanceRequestDto balanceRequestDto) {
        return loanApplicationService.getBalance(balanceRequestDto);
    }
}
