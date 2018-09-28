package net.zelinf.crypto_homework.finitefield.gf2;

import java.math.BigInteger;
import java.util.Objects;

public class GF2 {

    // the reducing polynomial
    private GF2Elem rp;

    public GF2(GF2Elem rp) {
        this.rp = rp;
    }

    public GF2Elem newElem(BigInteger value) {
        GF2Elem elem = new GF2Elem(value, this);
        if (value.bitLength() >= rp.getValue().bitLength())
            elem.normalize();
        return elem;
    }

    public static GF2Elem newBareElem(BigInteger value) {
        return new GF2Elem(value, null);
    }

    public GF2Elem getReducingPolynomial() {
        return rp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GF2 gf2 = (GF2) o;

        return Objects.equals(rp, gf2.rp);
    }
}
