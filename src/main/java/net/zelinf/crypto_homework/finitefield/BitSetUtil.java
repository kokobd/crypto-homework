package net.zelinf.crypto_homework.finitefield;

import java.util.BitSet;

public final class BitSetUtil {

    public static BitSet shiftLower(BitSet bitSet, int bits) {
        final int oldLength = bitSet.length();
        BitSet result = bitSet.get(bits, oldLength);
        result.set(oldLength, oldLength + bits);
        return result;
    }

    /**
     * Shift a {@code BitSet} higher for {@code bits}. <br>
     * This method doesn't modify the original {@code bitSet}.
     *
     * @param bitSet the {@code BitSet} to shift
     * @param bits   the shift distance, in number of bits
     * @return a newly created shifted {@code BitSet}
     */
    public static BitSet shiftHigher(BitSet bitSet, int bits) {
        BitSet result = (BitSet) bitSet.clone();
        final int length = result.length();
        for (int i = bits; i < length + bits; ++i) {
            result.set(i, bitSet.get(i - bits));
        }
        for (int i = 0; i < bits; ++i) {
            result.clear(i);
        }
        return result;
    }
}
