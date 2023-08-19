package com.yoaves.util.ConverTime;

import cn.hutool.core.date.DateUtil;
import com.yoaves.util.BaseConvertion.BaseConvertion;
import com.yoaves.util.ShuffleString.Shuffle;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Base64;

public class Base10ToBase64 {

    public static String base64String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

    @Getter
    @Setter
    private static Long millisec = System.currentTimeMillis();

    public static String getBase64String(Long time) {

//        Timestamp now = new Timestamp(time);
//
//        System.out.println(now);

        String longString = Long.toString(time);

//        System.out.println(longString);

        String baseString = Base64.getEncoder().encodeToString(new BigInteger(longString, 10).toByteArray());
        baseString = BaseConvertion.decToBase62(time);
        System.out.println("decimalToBase62: "+baseString);

//        checkBigIntegerAndByteAscii(longString);

        String lastChar = baseString.substring(baseString.length() - 1);
//        System.out.println("lastChar: " + lastChar);


        int digit = base64String.lastIndexOf(lastChar);//shuffling number for unshffled use.

        System.out.println("digit: " + digit);

        String shuffleNumber = Shuffle.shuffleString(baseString);
        System.out.println("shuffleNumber: "+shuffleNumber);
        String unshuffleNumber = Shuffle.unshuffleString(shuffleNumber);
        System.out.println("unshuffleNumber: "+unshuffleNumber);

        return shuffleNumber;

    }

    private static void checkBigIntegerAndByteAscii(String longString) {

        String base52ByYear = getBase52ByYear();
        BigInteger bigInteger = new BigInteger(base52ByYear, 10);
        System.out.println(bigInteger);

        System.out.println(longString);
        byte[] byteArray = new BigInteger(longString, 10).toByteArray();
        int i = 0;
        String byteString = new String(byteArray);
        for (byte b : byteArray) {
            i++;
            char c = byteString.charAt(i - 1);
            System.out.println(i + "\t byteArray: " + c + " "+ (int) c + " " + b);
        }
        String intFormBig = byteArray.toString();
        System.out.println("intFormBig: " + intFormBig);
    }

    private static String getBase52ByYear() {
        return String.valueOf(DateUtil.year(DateUtil.date()) - 2000);
    }

    public static int charToDecimal(char c) {
        if (c >= 'A' && c <= 'Z') {
            return (int) c - 'A';
        } else if (c >= 'a' && c <= 'z') {
            return (int) c - 'A' + 26;
        } else if (c >= '0' && c <= '9') {
            return (int) c - '0' + 52;
        }
        return -1;
    }


}
