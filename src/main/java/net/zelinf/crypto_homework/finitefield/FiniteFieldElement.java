package net.zelinf.crypto_homework.finitefield;

import org.apache.commons.lang3.builder.EqualsBuilder;

import java.util.BitSet;

public class FiniteFieldElement implements Cloneable {

    private BitSet bits;

    public FiniteFieldElement() {
        this(0);
    }

    public FiniteFieldElement(int length) {
        this(length, false);
    }

    public FiniteFieldElement(int length, boolean defaultValue) {
        bits = new BitSet(length);
        bits.set(0, length, defaultValue);
    }

    public FiniteFieldElement(FiniteFieldElement src) {
        bits = (BitSet) src.bits.clone();
    }

    public static FiniteFieldElement createOne() {
        return new FiniteFieldElement(1, true);
    }

    public static FiniteFieldElement createZero() {
        return new FiniteFieldElement();
    }

    public void set(int index, boolean value) {
        bits.set(index, value);
    }

    public void add(FiniteFieldElement another) {
        bits.xor(another.bits);
    }

    public void modulo(FiniteFieldElement mod) {
        while (bits.length() >= mod.bits.length()) {
            BitSet sub = BitSetUtil.shiftHigher(mod.bits, bits.length() - mod.bits.length());
            bits.xor(sub);
        }
    }

    public void multiply(FiniteFieldElement another, FiniteFieldElement mod) {
        BitSet product = new BitSet();

        for (int i = another.bits.nextSetBit(0); i != -1; i = another.bits.nextSetBit(i + 1)) {
            BitSet cur = (BitSet) bits.clone();
            product.xor(BitSetUtil.shiftHigher(cur, i));
        }

        bits = product;
        modulo(mod);
    }

    public void multiplicativeInvert(FiniteFieldElement mod) {
        // Please consult Algorithm 8 of Software Implementation of Elliptic Curve
        // Cryptography Over Binary Fields

        FiniteFieldElement b = FiniteFieldElement.createOne();
        FiniteFieldElement c = FiniteFieldElement.createZero();
        FiniteFieldElement u = null;
        FiniteFieldElement v = null;
        try {
            u = this.clone();
            v = mod.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        if (u == null || v == null)
            throw new IllegalStateException();

        while (u.degree() != 0) {
            int j = u.degree() - v.degree();
            if (j < 0) {
                FiniteFieldElement tmp = u;
                u = v;
                v = tmp;
                tmp = b;
                b = c;
                c = tmp;
                j = -j;
            }
            FiniteFieldElement x1 = new FiniteFieldElement();
            x1.set(j, true);
            FiniteFieldElement x2 = new FiniteFieldElement(x1);

            x1.multiply(v, mod);
            u.add(x1);
            u.modulo(mod);

            x2.multiply(c, mod);
            b.add(x2);
            b.modulo(mod);
        }

        this.bits = c.bits;
    }

    private int degree() {
        return bits.length();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < bits.length(); ++i) {
            boolean val = bits.get(i);
            builder.append(val ? '1' : '0');
        }
        builder.reverse();
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        FiniteFieldElement that = (FiniteFieldElement) o;

        return new EqualsBuilder()
                .append(bits, that.bits)
                .isEquals();
    }

    @Override
    protected FiniteFieldElement clone() throws CloneNotSupportedException {
        FiniteFieldElement clone = (FiniteFieldElement) super.clone();
        clone.bits = (BitSet) bits.clone();
        return clone;
    }
}
