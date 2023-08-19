package com.yoaves.util.ShuffleString;

import lombok.*;
import org.springframework.stereotype.Service;

@Service
public class Shuffle {

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    private static class InitValue{
        int lastCharOfString ;
        int indexRange ;
        int shuffleNum ;
        int minusStart ;
        int startFrom ;
        int untilEqual ;
    }

    public static String shuffleString(String s) {

        StringBuilder pickString = new StringBuilder();
        InitValue initValue = initValue(s);

        for (int i = initValue.getStartFrom(); i <= initValue.getUntilEqual(); i++) {
            int j = getShuffleLogic(initValue, i);
            pickString.append(s.charAt(j));
        }
        pickString.append((char) initValue.getLastCharOfString());

        return String.valueOf(pickString);

    }

    public static String unshuffleString(String s){

        char[] characters = new char[s.length()];
        InitValue initValue = initValue(s);

        for (int i = initValue.getStartFrom(); i <= initValue.getUntilEqual(); i++) {
            int j = getShuffleLogic(initValue, i);
            int index = i-1;
            characters[j] = s.charAt(index);
            /* test working method //for test
            System.out.print(index+" -> "+j +" : ");
            for (char c:characters){
                System.out.print(c +" ");
            }
            System.out.println("");
            //*/
        }
        characters[initValue.getIndexRange()] = (char) initValue.getLastCharOfString();

        return new String(characters);

    }

    private static int getShuffleLogic(InitValue initValue, int i) {
        return Math.abs(((initValue.minusStart - 1) * (initValue.getIndexRange() - 1)) + i * initValue.getShuffleNum() % initValue.getIndexRange());
    }

    private static InitValue initValue(String s) {

        int lastCharOfString = s.charAt(s.length()-1);//shffling base
        int indexRange = s.length() -1;//last char of string as suffle number
        int mopNum = indexRange - 1;//minus 1 make it not equal to indexRange
        int shuffleNum = (lastCharOfString % mopNum) + 1;//plus 1 make mop no equal to 0

        shuffleNum = isPrime(shuffleNum)
                &&indexRange%shuffleNum!=0
                ? shuffleNum:1;

        int minusStart = lastCharOfString % 2;
        int startFrom = 1;// - minusStart;
        int untilEqual = indexRange;// - minusStart;

        return InitValue.builder()
                .lastCharOfString(lastCharOfString)
                .indexRange(indexRange)
                .shuffleNum(shuffleNum)
                .minusStart(minusStart)
                .startFrom(startFrom)
                .untilEqual(untilEqual)
                .build();
    }

    private static boolean isPrime(int number) {
        if (number > 3) {
            for (int i = 2; i < number; i++) {
                if (number % i == 0) {
                    return false;
                }
            }
        }
        return true;
    }

}
