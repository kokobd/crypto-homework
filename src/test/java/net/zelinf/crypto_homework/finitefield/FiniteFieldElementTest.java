package net.zelinf.crypto_homework.finitefield;

import net.zelinf.crypto_homework.finitefield.gf2.GF2;
import net.zelinf.crypto_homework.finitefield.gf2.GF2Elem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class FiniteFieldElementTest {

    private static GF2 field1011;

    private static GF2 field10011;

    @BeforeAll
    static void initialize() {
        field1011 = new GF2(GF2.newBareElem(BigInteger.valueOf(0b1011)));
        field10011 = new GF2(GF2.newBareElem(BigInteger.valueOf(0b10011)));
    }

    @Test
    void testAdd() {
        testAddCase(field1011, 0b101, 0b110, 0b011);
        testAddCase(field1011, 0b010, 0b011, 0b001);
    }

    private void testAddCase(GF2 field, int x, int y, int result) {
        GF2Elem lhs = field.newElem(BigInteger.valueOf(x));
        GF2Elem rhs = field.newElem(BigInteger.valueOf(y));
        lhs.add(rhs);
        assertEquals(field.newElem(BigInteger.valueOf(result)), lhs);
    }

    @Test
    void testModulo() {
        testModuloCase(0b11011, 0b1011, 0b110);
    }

    private void testModuloCase(int num, int div, int result) {
        GF2 gf2 = new GF2(GF2.newBareElem(BigInteger.valueOf(div)));
        GF2Elem elem = gf2.newElem(BigInteger.valueOf(num));
        assertEquals(gf2.newElem(BigInteger.valueOf(result)), elem);
    }

    @Test
    void testMultiply() {
        testMultiplyCase(field1011, 0b111, 0b101, 0b110);
    }

    private void testMultiplyCase(GF2 field, int x, int y, int result) {
        GF2Elem x_ = field.newElem(BigInteger.valueOf(x));
        GF2Elem y_ = field.newElem(BigInteger.valueOf(y));
        assertEquals(field.newElem(BigInteger.valueOf(result)), x_.multiply(y_));
    }

    @Test
    void testInverse() {
        testInverseCase(field1011);
        testInverseCase(field10011);
    }

    private void testInverseCase(GF2 field) {
        final int rpLen = field.getReducingPolynomial().getValue().bitLength();
        final BigInteger upperBound = BigInteger.ONE.shiftRight(rpLen);
        final GF2Elem one = field.newElem(BigInteger.ONE);
        for (BigInteger x = BigInteger.ONE; x.compareTo(upperBound) < 0; x = x.add(BigInteger.ONE)) {
            GF2Elem elem = field.newElem(x);
            GF2Elem inverse = new GF2Elem(elem).mulInvert();

            assertEquals(one, elem.multiply(inverse));
        }
    }

}
