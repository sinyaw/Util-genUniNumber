package com.yoaves.util.genUniId;
import java.math.BigInteger;
import java.security.SecureRandom;

public class SHA256Logic {

    /**
     * RSA Key Generation:
     * 1)Key Pair Generation:
     * - Choose two large prime numbers, p and q.
     * - Calculate n = p * q.
     * - Calculate Euler's totient function φ(n) = (p-1) * (q-1).
     * - Choose a public exponent e such that 1 < e < φ(n) and e is coprime to φ(n).
     * - Calculate the private exponent d such that (d * e) % φ(n) = 1.
     * - Public Key (n, e): The public key consists of n and e.
     * 2)Private Key (n, d): The private key consists of n and d.
     * 3)RSA Encryption:
     * - Encrypting a Message:
     * - Convert the message into a numerical value m using a reversible encoding (like ASCII or Unicode).
     * - Calculate the ciphertext c using the formula: c = (m^e) % n.
     * RSA Decryption:
     * 1)Decrypting a Ciphertext:
     * - Calculate the plaintext m using the formula: m = (c^d) % n.
     *
     * Example: Suppose Alice wants to send a confidential message to Bob using RSA encryption. Here are the steps:
     * 1)Key Generation:
     * - Alice chooses p = 61 and q = 53.
     * - She calculates n = 61 * 53 = 3233.
     * - She calculates φ(n) = (61-1) * (53-1) = 3120.
     * - She chooses e = 17 as the public exponent.
     * - She calculates d = 2753 as the private exponent.
     * 2)Public Key (n, e): (3233, 17)
     * 3)Private Key (n, d): (3233, 2753)
     * 4)Encryption:
     * - Bob wants to send the message "HELLO" to Alice.
     * - He converts the message into numbers: H=72, E=69, L=76, O=79.
     * - The message becomes: m = 7269767679.
     * - He calculates ciphertext c = (m^17) % 3233.
     * 5)Decryption:
     * - Alice receives the ciphertext c.
     * - She calculates the plaintext using: plaintext m = (c^2753) % 3233.
     * - She converts the numerical message back to "HELLO".
     * This process ensures that only Bob can decrypt the ciphertext because only he knows the private key d.
     */

    public static void main(String[] args) {
        // Step 1: Key Generation
        int bits = 6;
        BigInteger p = generatePrime(bits);
        System.out.println("p: "+p);//exp: 1019
        BigInteger q = generatePrime(bits);
        System.out.println("q: "+q);//exp: 991
        BigInteger n = p.multiply(q);//modulus
        System.out.println("n: "+n);//exp: 1009829
        BigInteger phi_n = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        System.out.println("phi_n: "+phi_n);//exp: 1007820

        BigInteger e = BigInteger.valueOf(7); // A common choice for the public exponent
        System.out.println("e: "+e);//exp: 65537

        BigInteger d = e.modInverse(phi_n);//private exponent
        d = modInverse(e, phi_n);
        System.out.println("d: "+d);//exp: 834173

        // Original message
        BigInteger originalMessage = new BigInteger("9");//exp: 12345

        // Step 2: Encryption
        BigInteger encryptedMessage = originalMessage.modPow(e, n);//exp: 834173
        BigInteger powEN = originalMessage.pow(e.intValueExact());
        System.out.println("powEN: "+powEN);
        BigInteger encryptedMessage2 = powEN.mod(n);
        System.out.println("encryptedMessage2: "+encryptedMessage2);

        // Step 3: Decryption
        BigInteger decryptedMessage = encryptedMessage.modPow(d, n);//exp: 12345
        BigInteger powDN = encryptedMessage2.pow(d.intValueExact());
        System.out.println("powDN: "+powDN);
        BigInteger decryptedMessage2 = powDN.mod(n);
        System.out.println("decryptedMessage2: "+decryptedMessage2);

        // Print results
        System.out.println("Original Message: " + originalMessage);
        System.out.println("Encrypted Message: " + encryptedMessage);
        System.out.println("Decrypted Message: " + decryptedMessage);


//        BigInteger n = new BigInteger("54253");
//        BigInteger e = new BigInteger("65537");
//        BigInteger originalMessage = new BigInteger("12345");

//        BigInteger encryptedMessage = originalMessage.modPow(e, n);

    }

    private static BigInteger generatePrime(int bits) {
        SecureRandom random = new SecureRandom();
        return BigInteger.probablePrime(bits, random);
    }


    private static BigInteger generatePrime2(int bits) {
        SecureRandom random = new SecureRandom();
        while (true) {
            BigInteger num = new BigInteger(bits, random);
            if (num.compareTo(BigInteger.ONE) > 0 && isPrime(num)) {
                return num;
            }
        }
    }

    private static boolean isPrime(BigInteger n) {
        if (n.compareTo(BigInteger.ONE) <= 0) {
            return false;
        }
        if (n.compareTo(BigInteger.valueOf(3)) <= 0) {
            return true;
        }
        if (n.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO) ||
                n.mod(BigInteger.valueOf(3)).equals(BigInteger.ZERO)) {
            return false;
        }
        BigInteger i = BigInteger.valueOf(5);
        while (i.multiply(i).compareTo(n) <= 0) {
            if (n.mod(i).equals(BigInteger.ZERO) ||
                    n.mod(i.add(BigInteger.valueOf(2))).equals(BigInteger.ZERO)) {
                return false;
            }
            i = i.add(BigInteger.valueOf(6));
        }
        return true;
    }

    private static BigInteger modInverse(BigInteger publicExponent, BigInteger phi_n) {
        for (BigInteger x = BigInteger.ONE; x.compareTo(phi_n) < 0; x = x.add(BigInteger.ONE)) {
            if (publicExponent.multiply(x).mod(phi_n).equals(BigInteger.ONE)) {
                return x;
            }
        }
        return null;
    }
}
