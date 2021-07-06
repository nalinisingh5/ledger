package com.co.ledger.repository.impl;

import com.co.ledger.dto.BalanceRequestDto;
import com.co.ledger.dto.BalanceResponseDto;
import com.co.ledger.dto.PaymentRequestDto;
import com.co.ledger.model.EmiDrawdown;
import com.co.ledger.model.LedgerType;
import com.co.ledger.model.LoanApplication;
import com.co.ledger.repository.EmiDrawdownRepository;
import com.co.ledger.util.LoanUtils;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class EmiDrawdownRepositoryImpl implements EmiDrawdownRepository {

    //InMemory Map
    public static Map<String, List<EmiDrawdown>> emiDrawdownMap = new LinkedHashMap<>();

    @Override
    public void createEmiDrawdowns(LoanApplication loanApplication) {
        double roi = loanApplication.getRateOfInterest();
        double principalAmount = loanApplication.getPrincipalAmount();
        int noOfYears = loanApplication.getNoOfYears();
        double emi = Math.ceil(LoanUtils.calculateMonthlyEmi(roi, principalAmount, noOfYears));

        List<EmiDrawdown> emiDrawdownList = new LinkedList<>();
        for (int i = 1; i <= noOfYears * 12; i++) {
            EmiDrawdown emiDrawdowns = new EmiDrawdown();
            emiDrawdowns.setId();
            emiDrawdowns.setEmiNo(i);
            emiDrawdowns.setDrawdownAmount(emi);
            emiDrawdowns.setSettled(false);
            emiDrawdowns.setPaidExtra(false);
            emiDrawdowns.setExtraAmount(0.0);
            emiDrawdownList.add(emiDrawdowns);
        }

        if (!emiDrawdownMap.containsKey(loanApplication.getId())) {
            emiDrawdownMap.put(loanApplication.getId(), emiDrawdownList);
        }
    }

    @Override
    public String allocatePayments(PaymentRequestDto paymentRequestDto, Map<String, LoanApplication> loanApplicationMap) {
        String loanId = paymentRequestDto.getLoanId();
        if (!loanApplicationMap.containsKey(loanId)) {
            return "You don't have any active loans.";
        }

        if (!emiDrawdownMap.containsKey(loanId)) {
            // if loan exists but EmiDrawdowns not created then create them first
            LoanApplication loanApplication = loanApplicationMap.get(loanId);
            createEmiDrawdowns(loanApplication);
        }

        LoanApplication loanApplication = loanApplicationMap.get(loanId);
        if (paymentRequestDto.getLedgerType().equals(String.valueOf(LedgerType.PAYMENT)) && loanApplication.getBankName().equals(paymentRequestDto.getBankName()) && loanApplication.getBorrowerName().equals(paymentRequestDto.getBorrowerName())) {
            boolean changeMasterScheduleFlag = false;
            List<EmiDrawdown> oldEmiDrawdowns = emiDrawdownMap.get(loanId);
            List<EmiDrawdown> updateEmiDrawdowns = oldEmiDrawdowns;
            for (EmiDrawdown emiDrawdown : updateEmiDrawdowns) {
                if (emiDrawdown.getEmiNo() == paymentRequestDto.getEmiNo()) {
                    if (Double.compare(emiDrawdown.getDrawdownAmount(), paymentRequestDto.getLumpSumAmount()) == 0) {
                        emiDrawdown.setSettled(true);
                        emiDrawdown.setPaidExtra(false);
                        emiDrawdown.setExtraAmount(emiDrawdown.getExtraAmount());
                    }
                    if (Double.compare(emiDrawdown.getDrawdownAmount(), paymentRequestDto.getLumpSumAmount()) == -1) {
                        emiDrawdown.setSettled(true);
                        emiDrawdown.setPaidExtra(true);
                        emiDrawdown.setExtraAmount(paymentRequestDto.getLumpSumAmount() - emiDrawdown.getDrawdownAmount());
                        changeMasterScheduleFlag = true;
                    }

                    if (Double.compare(emiDrawdown.getDrawdownAmount(), paymentRequestDto.getLumpSumAmount()) == 1) {
                        emiDrawdown.setSettled(false);
                        emiDrawdown.setPaidExtra(false);

                        // adding as negative extra amount which will get added in next emi
                        emiDrawdown.setExtraAmount(-(paymentRequestDto.getLumpSumAmount() - emiDrawdown.getDrawdownAmount()));
                        changeMasterScheduleFlag = true;
                    }
                }
            }

            emiDrawdownMap.put(loanId, updateEmiDrawdowns);

            if (changeMasterScheduleFlag) {
                changeMasterSchedule(loanId, emiDrawdownMap);
            }
            return "Received Payment of " + paymentRequestDto.getLumpSumAmount() + " for EMI No " + paymentRequestDto.getEmiNo();
        }
        return "Borrower name or Bank name is mismatching";
    }

    private void changeMasterSchedule(String loanId, Map<String, List<EmiDrawdown>> emiDrawdownMap) {
        List<EmiDrawdown> emiDrawdownList = emiDrawdownMap.get(loanId);

        if (emiDrawdownList != null) {
            for (int i = 1; i < emiDrawdownList.size(); i++) {
                EmiDrawdown previousEmi = emiDrawdownList.get(i - 1);
                EmiDrawdown currentEmi = emiDrawdownList.get(i);
                // this if is for when the last emi paid lumpSump was less than emiAmount, in that case
                // create negative extra amount and subtracting it will add the difference which was paid
                // less last time in the current emi
                if (previousEmi.getSettled() == false && previousEmi.getSettled() == false && previousEmi.getExtraAmount() < 0.0) {
                    currentEmi.setDrawdownAmount(currentEmi.getDrawdownAmount() - (previousEmi.getExtraAmount()));
                }
                if (previousEmi.getSettled() == true && previousEmi.getPaidExtra() == true) {
                    if (Double.compare(previousEmi.getExtraAmount(), currentEmi.getDrawdownAmount()) == 0) {
                        previousEmi.setExtraAmount(0.0);
                        currentEmi.setSettled(true);
                    }
                    if (Double.compare(previousEmi.getExtraAmount(), currentEmi.getDrawdownAmount()) == 1) {
                        currentEmi.setSettled(true);
                        currentEmi.setPaidExtra(true);
                        currentEmi.setExtraAmount(previousEmi.getExtraAmount() - currentEmi.getDrawdownAmount());
                        previousEmi.setExtraAmount(0.0);
                    }
                }
            }
        }
    }

    @Override
    public BalanceResponseDto getBalance(BalanceRequestDto balanceRequestDto, LoanApplication loanApplication) {

        BalanceResponseDto balanceResponseDto = new BalanceResponseDto();
        String loanId = balanceRequestDto.getLoanId();
        if (!emiDrawdownMap.containsKey(loanId)) {
            createEmiDrawdowns(loanApplication);
            balanceResponseDto.setBankName(loanApplication.getBankName());
            balanceResponseDto.setBorrowerName(loanApplication.getBorrowerName());
            balanceResponseDto.setAmountPaid(0.0);
            balanceResponseDto.setNoOfEmisLeft(emiDrawdownMap.get(loanId).size());
            return balanceResponseDto;
        }

        if (balanceRequestDto.getLedgerType().equals(String.valueOf(LedgerType.BALANCE)) && loanApplication.getBankName().equals(balanceRequestDto.getBankName()) && loanApplication.getBorrowerName().equals(balanceRequestDto.getBorrowerName())) {
            List<EmiDrawdown> emiDrawdownList = emiDrawdownMap.get(loanId);
            double amountPaid = 0.0;
            int noOfEmisLeft = emiDrawdownList.size();
            for (EmiDrawdown emiDrawdown : emiDrawdownList) {
                balanceResponseDto.setBankName(loanApplication.getBankName());
                balanceResponseDto.setBorrowerName(loanApplication.getBorrowerName());
                if (emiDrawdown.getEmiNo() == balanceRequestDto.getEmiNo() && emiDrawdown.getSettled() == true) {
                    amountPaid += emiDrawdown.getDrawdownAmount();
                    noOfEmisLeft--;

                    break;
                }
                if (emiDrawdown.getEmiNo() < balanceRequestDto.getEmiNo() && emiDrawdown.getSettled() == true) {
                    amountPaid += emiDrawdown.getDrawdownAmount();
                    noOfEmisLeft--;
                }
            }

            balanceResponseDto.setAmountPaid(amountPaid);
            balanceResponseDto.setNoOfEmisLeft(noOfEmisLeft);

            return balanceResponseDto;
        } else return balanceResponseDto;
    }
}
