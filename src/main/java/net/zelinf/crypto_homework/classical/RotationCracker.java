package net.zelinf.crypto_homework.classical;

import java.util.*;
import java.util.stream.Stream;

public class RotationCracker {
    public RotationCracker(byte[] cipherText) {
        this.cipherText = cipherText;
    }

    private byte[] cipherText;

    public static class Result {
        private byte[] clearText;
        private int key;

        public Result(byte[] clearText, int key) {
            this.clearText = clearText;
            this.key = key;
        }

        public byte[] getClearText() {
            return clearText;
        }

        public int getKey() {
            return key;
        }
    }

    public Result crack() {
        final Map<Character, Integer> alphaOccurrences = new HashMap<>();
        int totalAlphas = 0;
        for (byte b : cipherText) {
            if (Character.isAlphabetic(b)) {
                alphaOccurrences.compute((char) b, (ch, times) -> times == null ? 1 : times + 1);
                totalAlphas++;
            }
        }
        final Map<Character, Double> cipherFrequencies = new HashMap<>();
        for (char ch = 'A'; ch <= 'Z'; ++ch) {
            cipherFrequencies.put(ch, (double) alphaOccurrences.getOrDefault(ch, 0) / totalAlphas);
        }

        final double STANDARD_VAL = 0.65;
        double minDiff = 1.0;
        int minDiffShift = 0;
        for (int shift = 0; shift < 26; ++shift) {
            final int shift_ = shift;
            final double val = Stream.iterate(0, x -> x + 1)
                    .limit(26)
                    .map(i -> alphaProb.get((char) ('A' + i))
                            * cipherFrequencies.get((char) ('A' + (i + shift_) % 26)))
                    .reduce(0.0, Double::sum);
            final double diff = Math.abs(val - STANDARD_VAL);
            if (diff < minDiff) {
                minDiff = diff;
                minDiffShift = shift;
            }
        }

        byte[] clearText = new byte[cipherText.length];
        for (int i = 0; i < cipherText.length; ++i) {
            clearText[i] = (byte) ('A' + ((cipherText[i] - 'A') + (26 - minDiffShift)) % 26);
        }

        return new Result(clearText, minDiffShift);
    }

    public static final Map<Character, Double> alphaProb = new HashMap<>();

    static {
        alphaProb.put('A', 0.082);
        alphaProb.put('B', 0.015);
        alphaProb.put('C', 0.028);
        alphaProb.put('D', 0.043);
        alphaProb.put('E', 0.126);
        alphaProb.put('F', 0.022);
        alphaProb.put('G', 0.020);
        alphaProb.put('H', 0.061);
        alphaProb.put('I', 0.070);
        alphaProb.put('J', 0.002);
        alphaProb.put('K', 0.008);
        alphaProb.put('L', 0.040);
        alphaProb.put('M', 0.024);
        alphaProb.put('N', 0.067);
        alphaProb.put('O', 0.075);
        alphaProb.put('P', 0.019);
        alphaProb.put('Q', 0.001);
        alphaProb.put('R', 0.060);
        alphaProb.put('S', 0.063);
        alphaProb.put('T', 0.091);
        alphaProb.put('U', 0.028);
        alphaProb.put('V', 0.010);
        alphaProb.put('W', 0.023);
        alphaProb.put('X', 0.001);
        alphaProb.put('Y', 0.020);
        alphaProb.put('Z', 0.001);
    }
}
