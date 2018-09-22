package net.zelinf.crypto_homework.classical.ex01;

import net.zelinf.crypto_homework.CmdUtils;
import net.zelinf.crypto_homework.Exercise;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.List;
import java.util.Scanner;

/**
 * Crack Virginia password
 */
public class Virginia implements Exercise {

    @Override
    public void run() {
        System.out.println("Input cipher text in a line: ");
        try (Scanner scanner = CmdUtils.scannerFromStdin()) {
            if (scanner.hasNextLine()) {
                String cipherText = scanner.nextLine().trim();

                VirginiaCracker cracker = new VirginiaCracker(cipherText);
                List<String> results = cracker.crack();

                System.out.println("Possible results: ");
                for (String result : results) {
                    System.out.printf("    %s", result);
                    System.out.println();
                }
            }
        }
    }

    @Override
    public ImmutablePair<Integer, Integer> getId() {
        return ImmutablePair.of(1, 1);
    }
}
