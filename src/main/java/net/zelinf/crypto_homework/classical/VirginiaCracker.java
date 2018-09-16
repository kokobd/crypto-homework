package net.zelinf.crypto_homework.classical;

import org.apache.commons.lang3.ArrayUtils;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

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
        List<Integer> possiblePeriods = possiblePeriods(cipherText.length / 100);
        return possiblePeriods.stream()
                .map(t -> {
                    List<byte[]> parts = new ArrayList<>(t);
                    for (int i = 0; i < t; ++i) {
                        final int length = cipherText.length / t + ((cipherText.length % t > i) ? 1 : 0);
                        byte[] part = new byte[length];
                        for (int j = 0; j < part.length; ++j) {
                            part[j] = cipherText[i + j * t];
                        }
                        parts.add(part);
                    }
                    List<byte[]> decryptedParts = parts.stream()
                            .map(cipher -> {
                                RotationCracker.Result result = new RotationCracker(cipher).crack();
                                return result.getClearText();
                            })
                            .collect(Collectors.toList());
                    byte[] result = new byte[cipherText.length];
                    for (int i = 0; i < result.length; ++i) {
                        result[i] = decryptedParts.get(i % t)[i / t];
                    }
                    return new String(result);
                }).collect(Collectors.toList());
    }

    private List<Integer> possiblePeriods_ = null;

    /**
     * Calculate possible periods
     *
     * @param errors maximum count of permitted errors in 'distances'
     * @return a list of possible periods. The most possible one comes first
     */
    private List<Integer> possiblePeriods(int errors) {
        if (possiblePeriods_ != null)
            return possiblePeriods_;

        List<Integer> distances = findDistances();

        Map<Integer, Integer> frequencies = new HashMap<>();
        for (int errorCount = 0; errorCount <= errors; ++errorCount) {
            // Remove 'errorCount' elements from distances,
            // then calculate the greatest common divisor

            List<Integer> indexToRemove = new ArrayList<>(errorCount);
            for (int i = 0; i < errorCount; ++i) {
                if (i == errorCount - 1) {
                    indexToRemove.add(i - 1);
                } else {
                    indexToRemove.add(i);
                }
            }

            if (indexToRemove.isEmpty()) {
                accumulateResult(distances, frequencies);
            } else {
                outerLoop:
                while (true) {
                    int k = indexToRemove.size() - 1;
                    while (indexToRemove.get(k) >= distances.size() - indexToRemove.size() + k) {
                        --k;
                        if (k == -1) {
                            break outerLoop;
                        }
                    }

                    int n = indexToRemove.get(k) + 1;
                    for (int j = k; j < indexToRemove.size(); ++j) {
                        indexToRemove.set(j, n);
                        ++n;
                    }

                    List<Integer> remains = new ArrayList<>();
                    for (int i = 0; i < distances.size(); ++i) {
                        if (!indexToRemove.contains(i)) {
                            remains.add(distances.get(i));
                        }
                    }
                    accumulateResult(remains, frequencies);
                }
            }
        }

        possiblePeriods_ = frequencies.entrySet().stream()
                .sorted((e1, e2) -> {
                    if (e1.getValue() > e2.getValue()) {
                        return -1;
                    } else if (e1.getValue() < e2.getValue()) {
                        return 1;
                    } else {
                        return 0;
                    }
                })
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        return possiblePeriods_;
    }

    private static void accumulateResult(List<Integer> numbers, Map<Integer, Integer> frequencies) {
        int result = gcdN(numbers);
        frequencies.compute(result, (key, oldValue) -> {
            int newValue = 1;
            if (oldValue != null) {
                newValue = oldValue + 1;
            }
            return newValue;
        });
    }

    private static int gcdN(Collection<Integer> numbers) {
        if (numbers.isEmpty()) {
            throw new IllegalArgumentException();
        }
        Iterator<Integer> numbersI = numbers.iterator();
        BigInteger result = BigInteger.valueOf(numbersI.next());
        while (numbersI.hasNext()) {
            result = result.gcd(BigInteger.valueOf(numbersI.next()));
        }
        return result.intValue();
    }

    private List<Integer> distances;

    private List<Integer> findDistances() {
        if (distances != null)
            return distances;
        distances = new ArrayList<>();
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
