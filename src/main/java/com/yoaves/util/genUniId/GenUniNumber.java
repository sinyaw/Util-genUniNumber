package com.yoaves.util.genUniId;

import cn.hutool.core.util.RandomUtil;

import java.util.*;

public class GenUniNumber {

    private HashMap<Long, Set<Long>> timePatternList;
    private List<Long> timeRecord;
    private List<List<Integer>> ints;

    public GenUniNumber() {
        this.timePatternList = new HashMap<>();
        this.timeRecord = new ArrayList<>();
        this.ints = genList();
    }

    private List<List<Integer>> genList() {
        return new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)),
                new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)),
                new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)))
        );
    }

    public Long getNumber() {
        Long modNum = 31557600000L;//Per Year(356.25)
        String index = null;
        Long timeNow = null;
        Long number = null;
        while (index == null) {
            timeNow = System.currentTimeMillis();
            number = timeNow % modNum;
            if (ints.size() == 0) {
                ints = genList();
                timeRecord.add(number);
            }
            if (timeRecord.contains(number)) {
                continue;
            }
            if (timeRecord.size() > 5) {
                timeRecord.remove(timeRecord.get(0));
            }
            List<Integer> intList = ints.get(0);
            Integer ind = RandomUtil.randomInt(ints.get(0).size());
            Integer num = ints.get(0).get(ind);
            //System.out.println("number: " + number + "  num: " + num);
            intList.remove(num);
            //System.out.println("ints: " + ints);
            Integer modTime = 3 - ints.size();
            if (intList.size() == 0) {
                ints.remove(intList);
            }
            //System.out.println("remove: " + remove);
            index = randomPattern(num, number + ((modTime) * modNum), modNum);
        }
        String year = String.valueOf(0 - 30 + timeNow / modNum);
        Set<Long> patternList = (timePatternList.get(number) != null) ? timePatternList.get(number) : new HashSet<>(Arrays.asList());
        patternList.add(Long.valueOf(year + index));
        timePatternList.put(number, patternList);
        return Long.valueOf(year + index);
    }

    private String randomPattern(int num, Long number, Long modNum) {
        String stringNumber = "";
        int frontZero = modNum.toString().length() - number.toString().length();
        StringBuilder stringLong = new StringBuilder();
        char[] numberChars = number.toString().toCharArray();
        for (int i = 0; i < frontZero; i++) {
            stringLong.append("0");
        }
        for (char c : numberChars) {
            stringLong.append(c);
        }
        stringNumber = stringLong.toString();

        //System.out.println("stringNumber: " + stringNumber);
        char[] c = stringNumber.toCharArray();
        stringLong = new StringBuilder();
        for (int i = 0; i < c.length; i++) {
            int j = i * 2 + num;
            int modNumber = mc(j);
            stringLong.append(c[modNumber]);
        }
        stringLong.append(num);
        return String.valueOf(stringLong);
    }

    private int mc(int number) {
        return number % 11;
    }

}
