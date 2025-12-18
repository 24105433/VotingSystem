package ph.robleding.votingsystem.ui;

import java.io.Console;
import java.util.Scanner;

public class PasswordField {

    private static final Scanner sharedScanner = new Scanner(System.in);

    public static String readPassword(String prompt) {
        Console console = System.console();

        if (console != null) {
            // ✅ Terminal supports masked input
            char[] passwordChars = console.readPassword(prompt);
            return new String(passwordChars);
        } else {
            // ⚠️ IDE fallback — visible input
            System.out.print(prompt + " ");
            return sharedScanner.nextLine();
        }
    }
}
