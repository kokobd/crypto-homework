package net.zelinf.crypto_homework.classical.ex02;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

class ModularArithmeticTest {

    @Test
    void testBezoutCoefficients() {
        Optional<Pair<Integer, Integer>> result = new ModularArithmetic(26).bezoutCoefficients(6, 13);
        assertEquals(Optional.of(Pair.of(11, 1)), result);
    }
}
