package net.zelinf.crypto_homework;

import org.apache.commons.io.input.CloseShieldInputStream;

import java.util.Scanner;

public class CmdUtils {

    public static Scanner scannerFromStdin() {
        return new Scanner(new CloseShieldInputStream(System.in));
    }
}
