package com.yoaves.util.genUniId;

import cn.hutool.core.date.DateUtil;
import com.yoaves.util.BaseConvertion.BaseConvertion;
import com.yoaves.util.ShuffleString.Shuffle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.Year;

@Service
public class GenUniString {

    @Autowired
    private static BaseConvertion baseConvert;

    @Autowired
    private static Shuffle shuffle;

    private static long recordTime = DateUtil.date().getTime();
    private static int waitNum = 0;//for test
    private static final int type = 0;//{0. millisecond 1. microsecond}
    private static final int base = 62;//for test

    public static void main(String[] args) {
        int i = 0;
        for (; ; ) {
            i++;
            String stringV1 = getStringV2();
            String stringV2 = changeStringIndex(stringV1, -1);
            String unshuffleString = Shuffle.unshuffleString(stringV2);
            System.out.println(i+"\t"+unshuffleString + " -> " + stringV2 + " -> " + stringV1);
//            System.out.println(getString());
        }
    }


    public static String getString() {
        long modNum = 31557600000L * (type == 0 ? 1 : 1000);//Per Year(356.25)
        int year = Year.now().hashCode() - 2000;//year minus 2000(start from 2000 year)

        long timeNow = getUniqueTime();

        long aYearDecimal = timeNow % modNum;

        String stringId = baseConvert.decToBase62(aYearDecimal);

        String shuffledId = shuffle.shuffleString(stringId);

        //23(year)+ m(millisecond) + Ur4UKJ(millisecond to base62 with shuffle)
        return year + changeStringIndex(shuffledId, 1);

    }

    public static String getStringV2() {
        long timeNow = getUniqueTime();

        String stringFullId = baseConvert.decToBase62(timeNow);
        String shuffledId = shuffle.shuffleString(stringFullId);

        return changeStringIndex(shuffledId, 1);
    }

    public static String changeStringIndex(String baseString, int dir) {//currently using base62 index
        BigInteger direction = new BigInteger(String.valueOf(dir));//dir (-1.backward or 1.forward)
        char[] shiftedString = new char[baseString.length()];

        char[] chars = baseString.toCharArray();//convert baseString to char array
        char lastChar = chars[chars.length - 1];//get the last char
        BigInteger shiftNumber = BaseConvertion.base62ToDec(String.valueOf(lastChar));//get back the base number
        shiftedString[chars.length - 1] = lastChar;//add the last char for return String

        for (int i = chars.length - 2; i >= 0; i--) {// shift index start from 2nd last char
            char followingChar = chars[i];
            BigInteger baseChar = BaseConvertion.base62ToDec(String.valueOf(followingChar));
            BigInteger numberWithDirection = shiftNumber.multiply(direction).add(new BigInteger(String.valueOf(i)).multiply(direction));//logic (shift index number)
            BigInteger shiftedIndex = baseChar.add(numberWithDirection).mod(new BigInteger("62"));//mod it to 62
            char[] baseConvertedChar = BaseConvertion.decToBase62(shiftedIndex.longValue()).toCharArray();
            char shiftedChar = baseConvertedChar[0];
            shiftedString[i] = shiftedChar;
        }

        return String.valueOf(shiftedString);
    }

    private static long getUniqueTime() {
        long timeNow = getTime(type);
//        int i = 0;//for test
//        waitNum++;//for test
        synchronized (new Object()) {
//            waitNum--;//for test
            while (Long.compare(timeNow, recordTime) <= 0) {
//                i++;//for test
                timeNow = getTime(type);
            }
            recordTime = timeNow;
        }
//        System.out.println("waitNum: "+waitNum+"\twhileNum: "+i+"\ttimeNow: "+timeNow);//for test
        return timeNow;
    }

    private static long getTime(int type) {
        switch (type) {
            case 0: //millisecond 13digits on 2023y
                return System.currentTimeMillis();
            case 1: //microsecond 16digits on 2023y
                long currentTimeMillis = System.currentTimeMillis();
                long nanoTime = System.nanoTime();
                long microTime = nanoTime / 1000; // Convert nanoseconds to microseconds
                long combinedTime = currentTimeMillis * 1_000_000 + microTime; // Combine milliseconds and microseconds
                return combinedTime / 1000;
            default:
                return System.currentTimeMillis();
        }
    }

}
