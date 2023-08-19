package com.yoaves.util.Byte;

import static com.yoaves.util.Byte.ShiftByte.leftShiftadd0;
import static com.yoaves.util.Byte.ShiftByte.leftShiftadd1;

public class GetBits {

    public static byte[] get8Bit(byte[] bytes) {
        return bytes;
    }

    public static byte[] get8Bit(char[] chars) {
        byte[] bytes = new byte[chars.length];
        for (int i = 0; i < chars.length; i++) {
            bytes[i] = get8Bit(chars[i]);
        }
        return bytes;
    }

    public static byte get8Bit(int i) {
        return get8Bit((byte) i);
    }

    public static byte get8Bit(char c) {
        return get8Bit((byte) c);
    }

    public static byte get8Bit(byte b) {
        return b;
    }

    public static void printAll8Bit(byte[] bytes) {
        for (byte b : bytes) {
            print8Bit(b);
        }
        System.out.println();
    }

    public static void print8Bit(byte myByte) {
        for (int i = 7; i >= 0; i--) {
            int bit = (myByte >> i) & 1;
            System.out.print(bit);
        }
        System.out.print(" ");
    }

    public static void main(String[] args) {
        byte value = (byte) 0b10101010; // Binary: 10101010

        byte resultLeftShift = leftShiftadd1(value);

        print8Bit(resultLeftShift);

        byte resultLeftShift2 = leftShiftadd0(resultLeftShift);

        print8Bit(resultLeftShift2);
//
//        byte a = get8Bit(0b101001011);
//        byte b = a;
//
//        int stop = 10;
//
//        while (stop > 0) {
//            int i = (b & 1);
//            if (i == 1) {
//                b = addBitOnRightShift(b);
//            } else {
//                b = addBitOnRightShiftUnsigned(b);
//            }
//            print8Bit(b);
//            System.out.println();
//            stop--;
//        }
    }

}
