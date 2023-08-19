package com.yoaves.util.Byte;

public class ShiftByte {

    public static byte moveBitRightToLeft(byte value) {
        if ((value & 1) == 1){
            return rightShiftadd1(value);
        }else{
            return rightShiftadd0(value);
        }
    }

    public static byte moveBitLeftToRight(byte value) {
        if (((value >> 7) & 1) == 1){
            return leftShiftadd1(value);
        }else{
            return leftShiftadd0(value);
        }
    }

    public static byte rightShiftadd1(byte value) {
        // Right shift the value by 1 and add 1 to the leftmost bit
        return (byte) ((value >> 1) | (1 << 7));
    }

    public static byte rightShiftadd0(byte value) {
        // Right shift the value by 1 without preserving the sign bit
        // Add 0 to the leftmost bit (it's already 0 due to unsigned shift)
        return (byte) ((value & 0xFF) >>> 1);
    }

    public static byte leftShiftadd1(byte value) {
        // Left shift the value by 1 and set the new leftmost bit
        return (byte) ((value << 1 & 0xFF) | 1);
    }

    public static byte leftShiftadd0(byte value) {
        // Left shift the value by 1 without changing the leftmost bit
        return (byte) (value << 1 & 0xFF);
    }

}
