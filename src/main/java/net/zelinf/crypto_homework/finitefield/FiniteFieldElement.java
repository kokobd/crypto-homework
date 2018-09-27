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
