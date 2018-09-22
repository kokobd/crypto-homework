package net.zelinf.crypto_homework.classical.ex01;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VirginiaCrackerTest {

    private static String cipherText;

    private static String clearText;

    @BeforeAll
    static void initialize() throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                VirginiaCrackerTest.class.getResourceAsStream("cipher_clear.txt")))) {
            cipherText = reader.readLine().trim();
            clearText = reader.readLine().trim();
        }
    }

    @Test
    void testCrack() {
        List<String> decryptedText = new VirginiaCracker(cipherText).crack();
        assertTrue(() -> decryptedText.stream().anyMatch(t -> t.equals(clearText)));
    }
}
