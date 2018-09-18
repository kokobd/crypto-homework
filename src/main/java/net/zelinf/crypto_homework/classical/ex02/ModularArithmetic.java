package net.zelinf.crypto_homework.classical.ex02;

import org.apache.commons.lang3.tuple.Pair;

import java.math.BigInteger;
import java.util.Objects;
import java.util.Optional;

public final class ModularArithmetic {
    public ModularArithmetic(int mod) {
        this.mod = mod;
    }

    private int mod;

    public int sum(int x, int y) {
        return (x + y) % mod;
    }

    public int subtract(int x, int y) {
        return sum(x, invert(y));
    }

    public int multiply(int x, int y) {
        return (x * y) % mod;
    }

    public Optional<Integer> multiplicativeInvert(int x) {
        int inverse = 0;
        try {
            inverse = BigInteger.valueOf(x).modInverse(BigInteger.valueOf(mod)).intValueExact();
        } catch (ArithmeticException e) {
            return Optional.empty();
        }
        return Optional.of(inverse);
    }

    public int invert(int x) {
        return mod - x;
    }

    public Optional<Pair<Integer, Integer>> bezoutCoefficients(int x, int y) {
        Optional<Integer> s = new ModularArithmetic(y).multiplicativeInvert(x);
        Optional<Integer> t = new ModularArithmetic(x).multiplicativeInvert(y);
        return s.flatMap(s_ -> t.flatMap(t_ -> Optional.of(Pair.of(s_, t_))));
    }

    public int getMod() {
        return mod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModularArithmetic that = (ModularArithmetic) o;
        return getMod() == that.getMod();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMod());
    }
}
