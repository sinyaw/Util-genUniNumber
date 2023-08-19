package com.yoaves.util.BaseConvertion;

import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class BaseConvertion {

    private static final String baseDigitsTil62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    /**
     * @param decimalNumber number to convert to the base62
     * @return string representation of the decimal number
     */
    public static String decToBase62(BigInteger decimalNumber) {
        return decToBase62(decimalNumber.longValue());
    }
    public static String decToBase62(long decimalNumber) {
        return fromDecimalToOtherBase(decimalNumber, 62);
    }

    public static BigInteger base62ToDec(String base62String) {
        return fromOtherBaseToDecimal(62, base62String);
    }

    private static String fromDecimalToOtherBase(long decimalNumber, int base) {
        String result = decimalNumber == 0 ? "0" : "";
        while (decimalNumber != 0) {
            int index = Math.toIntExact(decimalNumber % base);
            result = baseDigitsTil62.charAt(index) + result;
            decimalNumber = decimalNumber / base;
        }
        return result;
    }

    private static BigInteger fromOtherBaseToDecimal(int base, String baseString) {
        BigInteger totalValue = BigInteger.ZERO;

        for (char c : baseString.toCharArray()) {

            int power = (baseString.length() - 1) - baseString.indexOf(c);
            BigInteger num = new BigInteger(String.valueOf(baseDigitsTil62.indexOf(c)), 10);
            BigInteger bigInteger = new BigInteger(String.valueOf(base), 10);
            totalValue = totalValue.add(bigInteger.pow(power).multiply(num));

        }

        return totalValue;

    }

}
