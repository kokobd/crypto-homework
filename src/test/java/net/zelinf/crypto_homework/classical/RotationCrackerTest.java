package net.zelinf.crypto_homework.classical;

import net.zelinf.crypto_homework.classical.ex01.RotationCracker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RotationCrackerTest {

    @Test
    public void testAlphaProb() {
        double pSum = RotationCracker.alphaProb.values()
                .stream().reduce(0.0, (r, x) -> r + x);
        Assertions.assertEquals(1.0, pSum);
    }
}
