package util;

import java.io.Console;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;


public class PasswordUtil {


    public static String hash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erreur de hachage: " + e.getMessage());
        }
    }


    private static java.util.Scanner fallbackScanner = null;

    public static void setFallbackScanner(java.util.Scanner sc) {
        fallbackScanner = sc;
    }

    public static String readPassword(String prompt) {
        Console console = System.console();
        if (console != null) {
            char[] pwd = console.readPassword(prompt);
            return new String(pwd);
        }
        System.out.println(prompt);
        StringBuilder password = new StringBuilder();
        try {
            Process sttyOff = Runtime.getRuntime().exec(new String[]{"sh", "-c", "stty -echo -icanon min 1 < /dev/tty"});
            sttyOff.waitFor();

            FileInputStream tty = new FileInputStream("/dev/tty");
            int c;
            while ((c = tty.read()) != -1) {
                if (c == '\n' || c == '\r') {
                    break;
                } else if (c == 127 || c == '\b') {
                    if (password.length() > 0) {
                        password.deleteCharAt(password.length() - 1);
                        System.out.println("\b \b");
                        System.out.flush();
                    }
                } else if (c == 3) {
                    System.out.println();
                    Runtime.getRuntime().exec(new String[]{"sh", "-c", "stty echo icanon < /dev/tty"}).waitFor();
                    throw new RuntimeException("Saisie annulee");
                } else {
                    password.append((char) c);
                    System.out.println("*");
                    System.out.flush();
                }

            }
            tty.close();


        } catch (Exception e) {
            ConsoleUtil.info("Desole saisie masquee disponible uniquement sur le terminal de linux");
            if (fallbackScanner != null) {
                return fallbackScanner.nextLine();
            }
            return new Scanner(System.in).nextLine();
        }
        return password.toString();
    }

    public static boolean verify(String password, String hash) {
        return hash(password).equals(hash);
    }
}
