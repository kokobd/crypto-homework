package net.zelinf.crypto_homework.finitefield;

import org.junit.jupiter.api.Test;

import java.util.BitSet;

import static org.junit.jupiter.api.Assertions.*;

public class BitSetUtilTest {

    @Test
    void testShiftHigher() {
        BitSet bitSet = new BitSet();
        bitSet.set(0);
        bitSet.set(1);
        bitSet.set(3);

        bitSet = BitSetUtil.shiftHigher(bitSet, 1);

        BitSet expected = new BitSet();
        expected.set(1);
        expected.set(2);
        expected.set(4);
        assertEquals(expected, bitSet);
    }
}
