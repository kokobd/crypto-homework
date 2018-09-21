package net.zelinf.crypto_homework.classical.ex02;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AffineHillCrackerTest {

    @Test
    public void testGetKey() {
        final byte[] clearText = Utils.fromString("adisplayedequation");
        final byte[] cipherText = Utils.fromString("DSRMSIOPLXLJBZULLM");
        final int keyWidth = 3;
        final ModularIntegerMatrix expectedFactor = new ModularIntegerMatrix(AffineHillCracker.TEXT_SPACE_SIZE,
                new int[][]{
                        new int[]{3, 6, 4},
                        new int[]{5, 15, 18},
                        new int[]{17, 8, 5}
                });
        final RealVector expectedRemain = new ArrayRealVector(new double[]{8, 13, 1});

        AffineHillCracker cracker = new AffineHillCracker(clearText, cipherText, keyWidth);
        AffineHillCracker.Key actualKey = cracker.getKey();

        assertEquals(expectedFactor, actualKey.getFactor());
        assertEquals(expectedRemain, actualKey.getRemain());
    }

}
