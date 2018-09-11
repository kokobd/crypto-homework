package net.zelinf.crypto_homework.classical;

import java.nio.charset.Charset;
import java.util.Arrays;

public class VirginiaCracker {

    public VirginiaCracker(String cipherText) {
        this.cipherText = cipherText.getBytes();
    }

    private byte[] cipherText;

    public int calculatePeriod() {
        for (int len = 3; len <= cipherText.length / 2; ++len) {
            for (int i = 0; i <= cipherText.length - len; ++i) {
                // [i, j) is the currently selected interval
                final int j = i + len;

                // Compare interval [i, j) with all remaining
                // intervals that have same length
                for (int m = i + 1; m <= cipherText.length - len; ++i) {
                    final int n = m + len;

                    // Now we selected another interval [m, n)

                }
            }
        }

        throw new UnsupportedOperationException();
    }
}
