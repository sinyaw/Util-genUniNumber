package com.yoaves.util.Byte;

public class Char {

    public static char byteToChar(byte value) {
        // Cast the byte to a char
        return (char) value;
    }

    public static void printCharArray(char[] chars) {
        for (int i = 0; i < chars.length ; i++) {
            System.out.print(chars[i]);
        }
        System.out.print(" ");
    }

}
