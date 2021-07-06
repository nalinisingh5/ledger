package com.co.ledger;

import com.co.ledger.dto.BalanceRequestDto;
import com.co.ledger.dto.BalanceResponseDto;
import com.co.ledger.dto.LoanApplicationRequestDto;
import com.co.ledger.dto.PaymentRequestDto;
import com.co.ledger.service.LoanApplicationService;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LedgerApplicationUtil implements CommandLineRunner {

    @Autowired
    LoanApplicationService loanApplicationService;

    public LoanApplicationRequestDto readLoanApplicationRequestDtoInput() {

        String applyLoan1 = "";
        try {
            applyLoan1 = IOUtils.toString(LedgerApplicationUtil.class.getResourceAsStream("/1applyLoan.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        LoanApplicationRequestDto loanApplicationRequestDto = new Gson().fromJson(applyLoan1, LoanApplicationRequestDto.class);
        return loanApplicationRequestDto;
    }

    public PaymentRequestDto readPaymentRequestDtoInput() {

        String makePayment1 = "";
        try {
            makePayment1 = IOUtils.toString(LedgerApplicationUtil.class.getResourceAsStream("/1makePaymentEmi1.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        PaymentRequestDto paymentRequestDto = new Gson().fromJson(makePayment1, PaymentRequestDto.class);
        return paymentRequestDto;
    }

    public PaymentRequestDto readPaymentRequestDtoInput2() {

        String makePayment1 = "";
        try {
            makePayment1 = IOUtils.toString(LedgerApplicationUtil.class.getResourceAsStream("/1makePaymentEmi2.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        PaymentRequestDto paymentRequestDto = new Gson().fromJson(makePayment1, PaymentRequestDto.class);
        return paymentRequestDto;
    }

    public BalanceRequestDto readBalanceRequestDtoInput() {

        String getBalance1 = "";
        try {
            getBalance1 = IOUtils.toString(LedgerApplicationUtil.class.getResourceAsStream("/1getBalanceEmi1.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        BalanceRequestDto balanceRequestDto = new Gson().fromJson(getBalance1, BalanceRequestDto.class);
        return balanceRequestDto;
    }

    public BalanceRequestDto readBalanceRequestDtoInput2() {

        String getBalance1 = "";
        try {
            getBalance1 = IOUtils.toString(LedgerApplicationUtil.class.getResourceAsStream("/1getBalanceEmi3.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        BalanceRequestDto balanceRequestDto = new Gson().fromJson(getBalance1, BalanceRequestDto.class);
        return balanceRequestDto;
    }

    public LoanApplicationRequestDto readLoanApplication2RequestDtoInput1() {

        String applyLoan1 = "";
        try {
            applyLoan1 = IOUtils.toString(LedgerApplicationUtil.class.getResourceAsStream("/2applyLoan.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        LoanApplicationRequestDto loanApplicationRequestDto = new Gson().fromJson(applyLoan1, LoanApplicationRequestDto.class);
        return loanApplicationRequestDto;
    }

    public PaymentRequestDto readPaymentRequest2DtoInput1() {

        String makePayment1 = "";
        try {
            makePayment1 = IOUtils.toString(LedgerApplicationUtil.class.getResourceAsStream("/2makePaymentEmi1.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        PaymentRequestDto paymentRequestDto = new Gson().fromJson(makePayment1, PaymentRequestDto.class);
        return paymentRequestDto;
    }

    public BalanceRequestDto readBalanceRequestDto2Input24() {

        String getBalance1 = "";
        try {
            getBalance1 = IOUtils.toString(LedgerApplicationUtil.class.getResourceAsStream("/2getBalanceEmi24.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        BalanceRequestDto balanceRequestDto = new Gson().fromJson(getBalance1, BalanceRequestDto.class);
        return balanceRequestDto;
    }


    @Override
    public void run(String... args) throws Exception {
        // apply loan
        // make 1st payment with exact emi, 2nd payment with two times of 2nd emi
        // get balance after 1st payment, get balance for emiNo 3 after second payment of two emis
        testCase1();


        // apply loan
        // make 1 payment of full amount
        // get balance after 1st payment for last emi no
        testCase2();
    }

    private void testCase1() {
        System.out.println("Applying Loan ");
        LoanApplicationRequestDto loanApplicationRequestDto = readLoanApplicationRequestDtoInput();
        System.out.println(loanApplicationRequestDto.getLedgerType() + ", " + loanApplicationRequestDto.getBankName() + ", " + loanApplicationRequestDto.getBorrowerName() + ", " + loanApplicationRequestDto.getPrincipalAmount() + ", " + loanApplicationRequestDto.getNoOfYears() + ", " + loanApplicationRequestDto.getRateOfInterest());
        // apply loan and initiate loan and create schedules
        String loanApplicationResult = loanApplicationService.initiateLoan(loanApplicationRequestDto);
        System.out.println(loanApplicationResult);


        System.out.println("\nMake Payment 1");
        PaymentRequestDto paymentRequestDto = readPaymentRequestDtoInput();
        System.out.println(paymentRequestDto.getLedgerType() + ", " + paymentRequestDto.getBankName() + ", " + paymentRequestDto.getBorrowerName() + ", " + paymentRequestDto.getLumpSumAmount() + ", " + paymentRequestDto.getEmiNo());
        // make payment for loan if loan exists and allocate payment and if extra amount paid, then change master emiSchedules
        String paymentResult = loanApplicationService.makePayment(paymentRequestDto);
        System.out.println(paymentResult);

        System.out.println("\nGet Balance till Emi 1");
        BalanceRequestDto balanceRequestDto = readBalanceRequestDtoInput();
        System.out.println(balanceRequestDto.getLedgerType() + ", " + balanceRequestDto.getBankName() + ", " + balanceRequestDto.getBorrowerName() + ", " + balanceRequestDto.getEmiNo());
        BalanceResponseDto balanceResponseDto = loanApplicationService.getBalance(balanceRequestDto);
        System.out.println("Retrieving balance for emiNo " + balanceRequestDto.getEmiNo());
        System.out.println(balanceResponseDto.getBankName() + ", " + balanceResponseDto.getBorrowerName() + ", " + balanceResponseDto.getAmountPaid() + ", " + balanceResponseDto.getNoOfEmisLeft());

        System.out.println("\nMake Payment 2 with two times the monthly Emi Amount");
        PaymentRequestDto paymentRequestDto2 = readPaymentRequestDtoInput2();
        System.out.println(paymentRequestDto2.getLedgerType() + ", " + paymentRequestDto2.getBankName() + ", " + paymentRequestDto2.getBorrowerName() + ", " + paymentRequestDto2.getLumpSumAmount() + ", " + paymentRequestDto2.getEmiNo());
        // make payment for loan if loan exists and allocate payment and if extra amount paid, then change master emiSchedules
        String paymentResult2 = loanApplicationService.makePayment(paymentRequestDto2);
        System.out.println(paymentResult2);

        System.out.println("\nGet Balance till Emi 3, as second payment was made with 2 time actual emi, so if we try to get balance for emiNo 3 we should get 3*Emis");
        BalanceRequestDto balanceRequestDto2 = readBalanceRequestDtoInput2();
        System.out.println(balanceRequestDto2.getLedgerType() + ", " + balanceRequestDto2.getBankName() + ", " + balanceRequestDto2.getBorrowerName() + ", " + balanceRequestDto2.getEmiNo());
        BalanceResponseDto balanceResponseDto2 = loanApplicationService.getBalance(balanceRequestDto2);
        System.out.println("Retrieving balance for emiNo " + balanceRequestDto2.getEmiNo());
        System.out.println(balanceResponseDto2.getBankName() + ", " + balanceResponseDto2.getBorrowerName() + ", " + balanceResponseDto2.getAmountPaid() + ", " + balanceResponseDto2.getNoOfEmisLeft());
    }

    private void testCase2() {

        System.out.println("\n\n\nApplying Loan ");
        LoanApplicationRequestDto loanApplicationRequestDto = readLoanApplication2RequestDtoInput1();
        System.out.println(loanApplicationRequestDto.getLedgerType() + ", " + loanApplicationRequestDto.getBankName() + ", " + loanApplicationRequestDto.getBorrowerName() + ", " + loanApplicationRequestDto.getPrincipalAmount() + ", " + loanApplicationRequestDto.getNoOfYears() + ", " + loanApplicationRequestDto.getRateOfInterest());
        // apply loan and initiate loan and create schedules
        String loanApplicationResult = loanApplicationService.initiateLoan(loanApplicationRequestDto);
        System.out.println(loanApplicationResult);


        System.out.println("\nMake Payment 1 for entire loan amount");
        PaymentRequestDto paymentRequestDto = readPaymentRequest2DtoInput1();
        System.out.println(paymentRequestDto.getLedgerType() + ", " + paymentRequestDto.getBankName() + ", " + paymentRequestDto.getBorrowerName() + ", " + paymentRequestDto.getLumpSumAmount() + ", " + paymentRequestDto.getEmiNo());
        // make payment for loan if loan exists and allocate payment and if extra amount paid, then change master emiSchedules
        String paymentResult = loanApplicationService.makePayment(paymentRequestDto);
        System.out.println(paymentResult);

        System.out.println("\nGet Balance till Emi No 24");
        BalanceRequestDto balanceRequestDto = readBalanceRequestDto2Input24();
        System.out.println(balanceRequestDto.getLedgerType() + ", " + balanceRequestDto.getBankName() + ", " + balanceRequestDto.getBorrowerName() + ", " + balanceRequestDto.getEmiNo());
        BalanceResponseDto balanceResponseDto = loanApplicationService.getBalance(balanceRequestDto);
        System.out.println("Retrieving balance for emiNo " + balanceRequestDto.getEmiNo());
        System.out.println(balanceResponseDto.getBankName() + ", " + balanceResponseDto.getBorrowerName() + ", " + balanceResponseDto.getAmountPaid() + ", " + balanceResponseDto.getNoOfEmisLeft());
    }
}
