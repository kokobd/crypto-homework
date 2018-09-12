package net.zelinf.crypto_homework.classical;

import org.apache.commons.lang3.ArrayUtils;

import java.util.*;

/**
 * Cracker of Virginia password.
 * Note that this class is not thread safe.
 */
public class VirginiaCracker {

    public VirginiaCracker(String cipherText) {
        this.cipherText = cipherText.getBytes();
    }

    private byte[] cipherText;

    public List<String> crack() {
        return Collections.emptyList();
    }

    private List<Integer> possiblePeriods_ = null;

    /**
     * Calculate possible periods
     *
     * @param errors maximum count of permitted errors in 'distances'
     * @return a list of possible periods. The most possible one comes first
     */
    public List<Integer> possiblePeriods(int errors) {
        if (possiblePeriods_ != null)
            return possiblePeriods_;

        Set<Integer> distances = findDistances();

        Map<Integer, Integer> frequencies = new HashMap<>();
        for (int errorCount = 0; errorCount <= errors; ++errorCount) {

            BitSet selected = new BitSet();
            // TODO
        }

        return Collections.emptyList();
    }

    private Set<Integer> distances;

    public Set<Integer> findDistances() {
        if (distances != null)
            return distances;
        distances = new HashSet<>();
        for (int len = 3; len <= cipherText.length / 2; ++len) {
            for (int i = 0; i <= cipherText.length - len; ++i) {
                // [i, j) is the currently selected interval
                final int j = i + len;
                byte[] range_1 = ArrayUtils.subarray(cipherText, i, j);

                // Compare interval [i, j) with all remaining
                // intervals that have same length
                for (int m = i + 1; m <= cipherText.length - len; ++m) {
                    final int n = m + len;
                    // Now we selected another interval [m, n)
                    byte[] range_2 = ArrayUtils.subarray(cipherText, m, n);

                    if (Arrays.equals(range_1, range_2)) {
                        // Finally we found a 'distance'
                        final int distance = m - i;
                        distances.add(distance);
                    }
                }
            }
        }
        return distances;
    }

}
