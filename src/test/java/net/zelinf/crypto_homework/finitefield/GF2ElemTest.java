package net.zelinf.crypto_homework.finitefield;

import net.zelinf.crypto_homework.finitefield.gf2.GF2;
import net.zelinf.crypto_homework.finitefield.gf2.GF2Elem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigInteger;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class GF2ElemTest {

    private static GF2 field1011;

    private static GF2 field10011;

    @BeforeAll
    static void initialize() {
        field1011 = new GF2(GF2.newBareElem(BigInteger.valueOf(0b1011)));
        field10011 = new GF2(GF2.newBareElem(BigInteger.valueOf(0b10011)));
    }

    @ParameterizedTest
    @MethodSource("testAddProvider")
    void testAdd(GF2 field, int x, int y, int result) {
        GF2Elem lhs = field.newElem(BigInteger.valueOf(x));
        GF2Elem rhs = field.newElem(BigInteger.valueOf(y));
        lhs.add(rhs);
        assertEquals(field.newElem(BigInteger.valueOf(result)), lhs);
    }

    static Stream<Arguments> testAddProvider() {
        return Stream.of(
                Arguments.of(field1011, 0b101, 0b110, 0b011),
                Arguments.of(field1011, 0b010, 0b011, 0b001)
        );
    }

    @ParameterizedTest
    @MethodSource("testModuloProvider")
    void testModulo(int num, int div, int result) {
        GF2 gf2 = new GF2(GF2.newBareElem(BigInteger.valueOf(div)));
        GF2Elem elem = gf2.newElem(BigInteger.valueOf(num));
        assertEquals(gf2.newElem(BigInteger.valueOf(result)), elem);
    }

    static Stream<Arguments> testModuloProvider() {
        return Stream.of(
                Arguments.of(0b11011, 0b1011, 0b110)
        );
    }

    @ParameterizedTest
    @MethodSource("testMultiplyProvider")
    void testMultiply(GF2 field, int x, int y, int result) {
        GF2Elem x_ = field.newElem(BigInteger.valueOf(x));
        GF2Elem y_ = field.newElem(BigInteger.valueOf(y));
        assertEquals(field.newElem(BigInteger.valueOf(result)), x_.multiply(y_));
    }

    static Stream<Arguments> testMultiplyProvider() {
        return Stream.of(
                Arguments.of(field1011, 0b111, 0b101, 0b110)
        );
    }

    @ParameterizedTest
    @MethodSource("testInverseProvider")
    void testInverse(GF2 field) {
        final int rpLen = field.getReducingPolynomial().getValue().bitLength();
        final BigInteger upperBound = BigInteger.ONE.shiftLeft(rpLen - 1);
        final GF2Elem one = field.newElem(BigInteger.ONE);

        for (BigInteger x = BigInteger.ONE; x.compareTo(upperBound) < 0; x = x.add(BigInteger.ONE)) {
            GF2Elem elem = field.newElem(x);
            GF2Elem inverse = new GF2Elem(elem).mulInvert();

            String msg = String.format("elem: %s, inverse: %s", elem.toString(), inverse.toString());

            assertEquals(one, elem.multiply(inverse), msg);
        }
    }

    static Stream<Arguments> testInverseProvider() {
        return Stream.of(
                Arguments.of(field1011),
                Arguments.of(field10011)
        );
    }

}
