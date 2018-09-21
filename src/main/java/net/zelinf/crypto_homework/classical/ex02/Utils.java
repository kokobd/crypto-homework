package net.zelinf.crypto_homework.classical.ex02;

public final class Utils {

    public static byte[] fromString(String str) {
        str = str.toUpperCase();
        byte[] text = new byte[str.length()];
        for (int i = 0; i < str.length(); ++i) {
            text[i] = (byte) (str.charAt(i) - 'A');
        }
        return text;
    }
}
