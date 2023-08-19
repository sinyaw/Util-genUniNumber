package com.yoaves.util.genUniId;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Security;
import java.security.Signature;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

public class ECCLogic {

    /**
     * ECC Key Generation:
     * 1)Choose a Curve:
     * - Select an elliptic curve with known parameters (a, b) and a prime field p.
     * 2)Select a Base Point:
     * - Choose a point G on the curve as the base point.
     * 3)Choose a Private Key:
     * - Select a private key d as a random integer such that 1 < d < p.
     * 4)Calculate the Public Key:
     * - Compute the public key Q as Q = d * G.
     * ECC Encryption:
     * *ECC is primarily used for digital signatures and key exchange. It's not typically used for message encryption like RSA.
     * ECC Digital Signature:
     * 1)Signing a Message:
     * - Alice wants to sign a message M.
     * - Calculate a cryptographic hash of the message: hash = Hash(M).
     * - Compute the signature (r, s) using Alice's private key and the hash:
     *      - r = x coordinate of (k * G) % p, where k is a random integer.
     *      - s = (k^(-1)) * (hash + r * d) % p, where d is Alice's private key.
     * 2)Verifying the Signature:
     * - Bob receives the message M along with the signature (r, s) and Alice's public key Q.
     * - Bob calculates the hash of the message: hash = Hash(M).
     * - Calculate the point P = s^(-1) * hash * G + s^(-1) * r * Q.
     * - If r is equal to the x-coordinate of P, the signature is valid.
     *
     * Example:
     * 1)Key Generation:
     * - Choose the elliptic curve y^2 = x^3 + ax + b over a prime field p.
     * - Select the base point G(x, y).
     * - Choose a private key d.
     * - Compute the public key Q = d * G.
     * 2)Digital Signature:
     * - Alice wants to sign the message "HELLO".
     * - Calculate the hash of the message: hash = Hash("HELLO").
     * - Choose a random integer k.
     * - Calculate r = (k * G) % p.
     * - Calculate s = (k^(-1)) * (hash + r * d) % p.
     * - The signature is (r, s).
     * 3)Signature Verification:
     * - Bob receives the message "HELLO", the signature (r, s), and Alice's public key Q.
     * - Calculate the hash of the message: hash = Hash("HELLO").
     * - Calculate P = s^(-1) * hash * G + s^(-1) * r * Q.
     * - If r is equal to the x-coordinate of P, the signature is valid.
     */

    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        // Generate key pair using ECDSA
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", "BC");
        keyPairGenerator.initialize(ECNamedCurveTable.getParameterSpec("secp256r1")); // P-256 curve
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // Get private and public keys
        ECPrivateKey privateKey = (ECPrivateKey) keyPair.getPrivate();
        ECPublicKey publicKey = (ECPublicKey) keyPair.getPublic();

        // Message to be signed
        byte[] message = "Hello, ECC!".getBytes();

        // Sign the message using the private key
        // The signature is a pair of integers (r, s)
        byte[] signature = sign(message, privateKey);

        // Verify the signature using the public key
        boolean isVerified = verify(message, signature, publicKey);

        System.out.println("Message: " + new String(message));
        System.out.println("Signature: " + Hex.toHexString(signature));
        System.out.println("Signature Verified: " + isVerified);
    }

    public static byte[] sign(byte[] message, ECPrivateKey privateKey) throws Exception {
        Signature ecdsaSignature = Signature.getInstance("SHA256withECDSA");
        ecdsaSignature.initSign(privateKey);
        ecdsaSignature.update(message);
        return ecdsaSignature.sign();
    }

    public static boolean verify(byte[] message, byte[] signature, ECPublicKey publicKey) throws Exception {
        Signature ecdsaSignature = Signature.getInstance("SHA256withECDSA");
        ecdsaSignature.initVerify(publicKey);
        ecdsaSignature.update(message);
        return ecdsaSignature.verify(signature);
    }
}
