package net.zelinf.crypto_homework.classical.ex01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Crack Virginia password
 */
public class Main {

    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Main.class.getResourceAsStream("cipher.txt")))) {
            String cipherText = reader.readLine();
            VirginiaCracker cracker = new VirginiaCracker(cipherText);
            List<String> results = cracker.crack();
            System.out.println("Possible Results: ");
            for (String r : results) {
                System.out.println("\t" + r);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
