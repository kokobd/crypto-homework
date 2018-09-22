package net.zelinf.crypto_homework.classical.ex02;

import net.zelinf.crypto_homework.CmdUtils;
import net.zelinf.crypto_homework.Exercise;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.math3.linear.RealVector;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class AffineHill implements Exercise {

    @Override
    public void run() {
        try (Scanner scanner = CmdUtils.scannerFromStdin()) {
            System.out.println("Please input clear text in a line: ");
            String clearText = scanner.nextLine().trim();

            System.out.println("Please input cipher text in a line: ");
            String cipherText = scanner.nextLine().trim();

            System.out.print("Key width: ");
            int keyWidth = scanner.nextInt();

            if (clearText.length() == 0 || cipherText.length() == 0 || keyWidth <= 0)
                throw new NoSuchElementException();
            try {
                AffineHillCracker cracker = new AffineHillCracker(
                        Utils.fromString(clearText), Utils.fromString(cipherText), keyWidth);
                printKey(cracker.getKey());
            } catch (FailedToInferKeyException e) {
                System.out.println("Can not infer key from the input.");
            }

        } catch (NoSuchElementException e) {
            System.out.println("Illegal input.");
        }
    }

    @Override
    public ImmutablePair<Integer, Integer> getId() {
        return ImmutablePair.of(1, 2);
    }

    private static void printKey(AffineHillCracker.Key key) {
        final ModularIntegerMatrix factor = key.getFactor();
        final RealVector remain = key.getRemain();

        System.out.println("L: ");
        for (int i = 0; i < factor.getRowDimension(); ++i) {
            for (int j = 0; j < factor.getColumnDimension(); ++j) {
                System.out.printf("%d ", factor.getEntry(i, j));
            }
            System.out.println();
        }

        System.out.println("B: ");
        for (int i = 0; i < remain.getDimension(); ++i) {
            System.out.printf("%d ", (int) remain.getEntry(i));
        }
        System.out.println();
    }
}
