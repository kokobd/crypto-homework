package net.zelinf.crypto_homework.finitefield;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.JRE;

import static org.junit.jupiter.api.Assertions.*;

class FiniteFieldElementTest {

    @Test
    void testAdd() {
        assertAll(() -> {
            FiniteFieldElement op1 = fromString("01010011");
            FiniteFieldElement op2 = fromString("11001010");
            FiniteFieldElement expected = fromString("10011001");

            FiniteFieldElement actual = new FiniteFieldElement(op1);
            actual.add(op2);

            assertEquals(expected, actual);
        }, () -> {
            FiniteFieldElement op1 = fromString("111");
            FiniteFieldElement op2 = fromString("11");
            FiniteFieldElement expected = fromString("100");

            FiniteFieldElement actual = new FiniteFieldElement(op1);
            actual.add(op2);

            assertEquals(expected, actual);
        });
    }

    @Test
    void testModulo() {
        FiniteFieldElement num = fromString("11011");
        FiniteFieldElement div = fromString("1011");
        num.modulo(div);

        assertEquals(fromString("110"), num);
    }

    @Test
    void testMultiply() {
        assertMultiplication("111", "101", "1011", "110");
    }

    private static void assertMultiplication(String lhs, String rhs, String mod, String expectedResult) {
        FiniteFieldElement x = fromString(lhs);
        FiniteFieldElement y = fromString(rhs);
        x.multiply(y, fromString(mod));
        assertEquals(fromString(expectedResult), x);
    }

    @Test
    void testInverse() {
        assertInversion("1", "1011");
        assertInversion("10", "1011");
        assertInversion("11", "1011");
        assertInversion("100", "1011");
        assertInversion("101", "1011");
        assertInversion("110", "1011");
        assertInversion("111", "1011");

        assertInversion("1", "10011");
        assertInversion("10", "10011");
        assertInversion("11", "10011");
        assertInversion("100", "10011");
        assertInversion("101", "10011");
        assertInversion("110", "10011");
        assertInversion("111", "10011");
        assertInversion("1000", "10011");
        assertInversion("1001", "10011");
        assertInversion("1010", "10011");
    }

    private static void assertInversion(String num, String mod) {
        FiniteFieldElement mod_ = fromString(mod);
        FiniteFieldElement num_ = fromString(num);
        FiniteFieldElement inverse = new FiniteFieldElement(num_);
        inverse.multiplicativeInvert(mod_);
        num_.multiply(inverse, mod_);
        assertEquals(FiniteFieldElement.createOne(), num_);
    }

    public static FiniteFieldElement fromString(String str) {
        str = new StringBuilder(str).reverse().toString();

        FiniteFieldElement element = new FiniteFieldElement(str.length());
        for (int i = 0; i < str.length(); ++i) {
            char ch = str.charAt(i);
            element.set(i, ch == '1');
        }
        return element;
    }

    @Test
    @EnabledOnJre({JRE.JAVA_8})
    void test() {

    }
}
