package com.co.ledger.util;

import org.springframework.stereotype.Component;

@Component
public class LoanUtils {

    public static double calculateMonthlyEmi(double rateOfInterest, double principalAmount, int noOfYears) {

        int noOfMonths = noOfYears*12;
        if (rateOfInterest > 0) {
            double R = rateOfInterest / (12 * 100);
            double top = principalAmount * R * Math.pow((1 + R), noOfMonths);
            double bottom = Math.pow((1 + R), noOfMonths) - 1;
            return top / bottom;
        } else if (rateOfInterest == 0) {
            return principalAmount / noOfMonths;
        }
        return 0;
    }
}
