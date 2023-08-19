package com.yoaves.util.genUniId;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

import static com.yoaves.util.Byte.GetBits.get8Bit;


public class MD5Logic {

    public static void main(String[] args) {

        String input = "Hello, this is a longer message with 65 characters. still need to add. ";
        String abcd = calculateMD5(input + input);
        System.out.println(abcd);

    }

    private static final int BLOCK_SIZE = 64; // 512 bits in bytes

    // Constants for MD5 operations
    private static final int[] R = {
            3, 7, 11, 19, 3, 7, 11, 19, 3, 7, 11, 19, 3, 7, 11, 19,
            3, 5, 9, 13, 3, 5, 9, 13, 3, 5, 9, 13, 3, 5, 9, 13,
            3, 9, 11, 15, 3, 9, 11, 15, 3, 9, 11, 15, 3, 9, 11, 15,
            3, 7, 11, 19, 3, 7, 11, 19, 3, 7, 11, 19, 3, 7, 11, 19
    };

    private static final int[] K = new int[64];

    static {
        for (int i = 0; i < 64; i++) {
            K[i] = (int) (Math.abs(Math.sin(i + 1)) * (1L << 32));
        }
    }

    private static byte[] padMessage(byte[] message) {
        int initialLength = message.length;
        int paddingLength = BLOCK_SIZE - (initialLength % BLOCK_SIZE);
        int paddedLength = initialLength + paddingLength;

        byte[] paddedMessage = new byte[paddedLength];
        System.arraycopy(message, 0, paddedMessage, 0, initialLength);

        // Append single '1' bit
        paddedMessage[initialLength] = (byte) 0x80;

        // Append length in bits as 64-bit little-endian integer
        long messageLengthBits = (long) initialLength * 8;
        for (int i = 0; i < 8; i++) {
            paddedMessage[paddedLength - 8 + i] = (byte) (messageLengthBits >>> (i * 8));
        }

        return paddedMessage;
    }

    private static int[] initializeHashValues() {
        return new int[]{0x67452301, 0xEFCDAB89, 0x98BADCFE, 0x10325476};
    }

    private static int[] createWordArray(byte[] message, int start) {
        int[] words = new int[16];
        for (int i = 0; i < 16; i++) {
            int word = 0;
            for (int j = 0; j < 4; j++) {
                word |= (message[start + i * 4 + j] & 0xFF) << (j * 8);
//                System.out.println("Word after combining: 0x" + Integer.toHexString(word).toUpperCase());
            }
            words[i] = word;
//            printIntArray(words);
        }
        return words;
    }

    private static int leftRotate(int x, int count) {
        return (x << count) | (x >>> (32 - count));
    }

    private static int[] processBlock(int[] block, int[] state) {
        int a = state[0];
        int b = state[1];
        int c = state[2];
        int d = state[3];

        for (int i = 0; i < 64; i++) {
            int f, g;

            if (i < 16) {
                f = (b & c) | ((~b) & d);
                g = i;
            } else if (i < 32) {
                f = (d & b) | ((~d) & c);
                g = (5 * i + 1) % 16;
            } else if (i < 48) {
                f = b ^ c ^ d;
                g = (3 * i + 5) % 16;
            } else {
                f = c ^ (b | (~d));
                g = (7 * i) % 16;
            }

            int temp = d;
            d = c;
            c = b;
            b = b + leftRotate((a + f + K[i] + block[g]), R[i]);
            a = temp;
        }

        state[0] += a;
        state[1] += b;
        state[2] += c;
        state[3] += d;

        return state;
    }

    public static String calculateMD5(String input) {
        byte[] message = input.getBytes(StandardCharsets.UTF_8);
//        printByteArray(message);

        int[] hashState = initializeHashValues();

        byte[] paddedMessage = padMessage(message);
//        printHexArray(paddedMessage);


        int numBlocks = paddedMessage.length / BLOCK_SIZE;
//        System.out.println("numBlocks: " + numBlocks);
        for (int i = 0; i < numBlocks; i++) {
            int[] block = createWordArray(paddedMessage, i * BLOCK_SIZE);
            hashState = processBlock(block, hashState);
        }


        StringBuilder result = new StringBuilder();
        for (int value : hashState) {
            result.append(String.format("%02x", value));
        }

        return result.toString();
    }

    private static void printHexArray(byte[] bytes) {
        int columns = 8;
        int tempColumns = columns;
        for (byte b : bytes) {
            tempColumns--;
            String intString = new BigInteger(String.valueOf(b),10).toString(16);
            System.out.print(intString);
            for (int i = 0; i < 5 - intString.length(); i++) {
                System.out.print(" ");
            }
//            get8Bit(b);
            if (tempColumns <= 0) {
                System.out.println();
                tempColumns = columns;
            }
        }
        System.out.println();
    }

    private static void printIntArray(int[] ints) {
        int columns = 16;
        int tempColumns = columns;
        // Print the contents of the array
        for (int i = 0; i < ints.length; i++) {
            tempColumns--;
            System.out.print(Integer.toHexString(ints[i]).toUpperCase()+" ");
            if (tempColumns <= 0) {
                System.out.println();
                tempColumns = columns;
            }
        }
        System.out.println();
    }

}

