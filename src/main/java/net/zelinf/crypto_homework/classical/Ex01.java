package net.zelinf.crypto_homework.classical;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;

/**
 * Crack Virginia password
 */
public class Ex01 {

    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Ex01.class.getResourceAsStream("cipher.txt")))) {
            String cipherText = reader.readLine();
            VirginiaCracker cracker = new VirginiaCracker(cipherText);
            Set<Integer> distances = cracker.findDistances();
            System.out.println(distances);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
