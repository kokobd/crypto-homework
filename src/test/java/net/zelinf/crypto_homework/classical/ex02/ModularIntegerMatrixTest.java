package net.zelinf.crypto_homework.classical.ex02;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ModularIntegerMatrixTest {

    @Test
    void testInverse() {
        final ModularIntegerMatrix matrix = new ModularIntegerMatrix(26,
                new int[][]{
                        new int[]{6, 24, 1},
                        new int[]{13, 16, 10},
                        new int[]{20, 17, 15}
                }
        );
        final ModularIntegerMatrix expectedResult = new ModularIntegerMatrix(26,
                new int[][]{
                        new int[]{8, 5, 10},
                        new int[]{21, 8, 21},
                        new int[]{21, 12, 8}
                });
        assertEquals(Optional.of(expectedResult), matrix.getInverse());
    }

}
