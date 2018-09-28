package net.zelinf.crypto_homework.finitefield;

import net.zelinf.crypto_homework.CmdUtils;
import net.zelinf.crypto_homework.Exercise;
import net.zelinf.crypto_homework.finitefield.gf2.GF2;
import net.zelinf.crypto_homework.finitefield.gf2.GF2Elem;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.math.BigInteger;
import java.util.Scanner;

public class FiniteFieldDemo implements Exercise {

    @Override
    public void run() {

        try (Scanner sc = CmdUtils.scannerFromStdin()) {
            System.out.println("Please input a number to invert: ");
            if (sc.hasNextBigInteger()) {
                BigInteger input = sc.nextBigInteger();

                GF2Elem rp = GF2.newBareElem(
                        BigInteger.valueOf(0b111)
                                .xor(BigInteger.ONE.shiftLeft(13))
                                .xor(BigInteger.ONE.shiftLeft(131))
                );
                GF2 field = new GF2(rp);
                GF2Elem inputInverse = field.newElem(input).mulInvert();
                System.out.println("The inverse is: ");
                System.out.println(inputInverse);
            } else {
                System.out.println("Invalid input.");
            }
        }
    }

    @Override
    public ImmutablePair<Integer, Integer> getId() {
        return ImmutablePair.of(2, 1);
    }
}
