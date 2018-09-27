package net.zelinf.crypto_homework.finitefield;

import org.junit.jupiter.api.Test;

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

    public static FiniteFieldElement fromString(String str) {
        str = new StringBuilder(str).reverse().toString();

        FiniteFieldElement element = new FiniteFieldElement(str.length());
        for (int i = 0; i < str.length(); ++i) {
            char ch = str.charAt(i);
            element.set(i, ch == '1');
        }
        return element;
    }

}
