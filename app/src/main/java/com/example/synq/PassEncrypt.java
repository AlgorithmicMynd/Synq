package com.example.synq;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class PassEncrypt{

    // Initial hash values (first 32 bits of square roots of first 8 primes)
    private static final int[] H = {
            0x6a09e667, 0xbb67ae85, 0x3c6ef372, 0xa54ff53a,
            0x510e527f, 0x9b05688c, 0x1f83d9ab, 0x5be0cd19
    };

    // SHA-256 round constants (first 32 bits of cube roots of first 64 primes)
    private static final int[] K = {
            0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5dba5, 0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5,
            0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3, 0x72be5d74, 0x80deb1fe, 0x9bdc06a7, 0xc19bf174,
            0xe49b69c1, 0xefbe4786, 0x0fc19dc6, 0x240ca1cc, 0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da,
            0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7, 0xc6e00bf3, 0xd5a79147, 0x06ca6351, 0x14292967,
            0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13, 0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85,
            0xa2bfe8a1, 0xa81a664b, 0xc24b8b70, 0xc76c51a3, 0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070,
            0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3,
            0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208, 0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2
    };

    // Right rotate function
    private static int rightRotate(int value, int bits) {
        return (value >>> bits) | (value << (32 - bits));
    }

    // SHA-256 σ0 and σ1 functions
    private static int sigma0(int x) {
        return rightRotate(x, 7) ^ rightRotate(x, 18) ^ (x >>> 3);
    }

    private static int sigma1(int x) {
        return rightRotate(x, 17) ^ rightRotate(x, 19) ^ (x >>> 10);
    }

    // SHA-256 compression functions
    private static int ch(int x, int y, int z) {
        return (x & y) ^ (~x & z);
    }

    private static int maj(int x, int y, int z) {
        return (x & y) ^ (x & z) ^ (y & z);
    }

    private static int sum0(int x) {
        return rightRotate(x, 2) ^ rightRotate(x, 13) ^ rightRotate(x, 22);
    }

    private static int sum1(int x) {
        return rightRotate(x, 6) ^ rightRotate(x, 11) ^ rightRotate(x, 25);
    }

    // Pad the message to 512-bit blocks
    private static byte[] padMessage(byte[] message) {
        int originalLength = message.length;
        int newLength = originalLength + 1; // Add 1-bit (0x80)
        while ((newLength % 64) != 56) {
            newLength++;
        }
        byte[] padded = Arrays.copyOf(message, newLength + 8);
        padded[originalLength] = (byte) 0x80; // Append 1-bit (10000000)

        long bitLength = (long) originalLength * 8;
        for (int i = 0; i < 8; i++) {
            padded[padded.length - 1 - i] = (byte) (bitLength >>> (8 * i));
        }
        return padded;
    }

    // SHA-256 Main Hashing Algorithm
    public static String sha256(String input) {
        byte[] message = padMessage(input.getBytes(StandardCharsets.UTF_8));

        int[] hashValues = Arrays.copyOf(H, H.length);
        int[] words = new int[64];

        for (int i = 0; i < message.length; i += 64) {
            ByteBuffer buffer = ByteBuffer.wrap(message, i, 64);
            for (int j = 0; j < 16; j++) {
                words[j] = buffer.getInt();
            }

            // Expand words W16–W63
            for (int j = 16; j < 64; j++) {
                words[j] = sigma1(words[j - 2]) + words[j - 7] + sigma0(words[j - 15]) + words[j - 16];
            }

            int a = hashValues[0], b = hashValues[1], c = hashValues[2], d = hashValues[3];
            int e = hashValues[4], f = hashValues[5], g = hashValues[6], h = hashValues[7];

            for (int j = 0; j < 64; j++) {
                int temp1 = h + sum1(e) + ch(e, f, g) + K[j] + words[j];
                int temp2 = sum0(a) + maj(a, b, c);

                h = g;
                g = f;
                f = e;
                e = d + temp1;
                d = c;
                c = b;
                b = a;
                a = temp1 + temp2;
            }

            hashValues[0] += a;
            hashValues[1] += b;
            hashValues[2] += c;
            hashValues[3] += d;
            hashValues[4] += e;
            hashValues[5] += f;
            hashValues[6] += g;
            hashValues[7] += h;
        }

        // Convert hash values to hexadecimal
        StringBuilder hash = new StringBuilder();
        for (int hVal : hashValues) {
            hash.append(String.format("%08x", hVal));
        }
        return hash.toString();
    }


}