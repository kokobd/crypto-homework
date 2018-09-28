package net.zelinf.crypto_homework.finitefield.gf2;

import net.zelinf.crypto_homework.common.CopyAssignable;
import net.zelinf.crypto_homework.common.Utils;

import java.math.BigInteger;
import java.util.Objects;

public class GF2Elem implements CopyAssignable<GF2Elem> {

    private GF2 field;

    private BigInteger value;

    GF2Elem(BigInteger value, GF2 field) {
        this.value = value;
        this.field = field;
        normalize();
    }

    /**
     * Copy constructor
     *
     * @param elem copies {@code elem} into this object
     */
    public GF2Elem(GF2Elem elem) {
        copyAssign(elem);
    }

    @Override
    public void copyAssign(GF2Elem src) {
        this.field = src.field;
        this.value = src.value;
    }

    public void setValue(BigInteger value) {
        if (value.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
        this.value = value;
        normalize();
    }

    public BigInteger getValue() {
        return value;
    }

    public void setField(GF2 field) {
        this.field = field;
        normalize();
    }

    public GF2 getField() {
        return field;
    }

    private int cardinality() {
        return value.bitLength();
    }

    public void normalize() {
        if (field != null) {
            final GF2Elem rp = field.getReducingPolynomial();
            while (value.bitLength() >= rp.value.bitLength()) {
                BigInteger sub = rp.value.shiftLeft(value.bitLength() - rp.value.bitLength());
                value = value.xor(sub);
            }
        }
    }

    public GF2Elem add(GF2Elem elem) {
        checkField(elem);
        value = value.xor(elem.getValue());
        return this;
    }

    public GF2Elem multiply(GF2Elem elem) {
        checkField(elem);

        BigInteger sum = BigInteger.ZERO;
        for (int i = 0; i < elem.value.bitLength(); ++i) {
            if (elem.value.testBit(i)) {
                sum = sum.xor(value.shiftLeft(i));
            }
        }
        value = sum;

        normalize();
        return this;
    }

    public GF2Elem square() {
        return new GF2Elem(this).multiply(this);
    }

    public GF2Elem mulInvert() {
        if (field == null) {
            return null;
        }

        GF2Elem b = new GF2Elem(BigInteger.ONE, null);
        GF2Elem c = new GF2Elem(BigInteger.ZERO, null);
        GF2Elem u = new GF2Elem(this);
        u.setField(null);
        GF2Elem v = new GF2Elem(field.getReducingPolynomial());

        while (u.cardinality() != 0) {
            int j = u.cardinality() - v.cardinality();
            if (j < 0) {
                u = Utils.returnFirst(v, v = u);
                b = Utils.returnFirst(c, c = b);
                j = -j;
            }
            GF2Elem x1 = new GF2Elem(BigInteger.ZERO, null);
            x1.value = x1.value.setBit(j);
            GF2Elem x2 = new GF2Elem(x1);

            x1.multiply(v);
            u.add(x1);
            u.setField(field);
            u.setField(null);

            x2.multiply(c);
            b.add(x2);
            b.setField(field);
            b.setField(null);
        }

        copyAssign(c);
        return this;
    }

    private void checkField(GF2Elem elem) {
        if (!Objects.equals(field, elem.field)) {
            throw new FieldNotMatchException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GF2Elem gf2Elem = (GF2Elem) o;
        return Objects.equals(field, gf2Elem.field) &&
                Objects.equals(getValue(), gf2Elem.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(field, getValue());
    }

    @Override
    public String toString() {
        return value.toString(2);
    }
}
