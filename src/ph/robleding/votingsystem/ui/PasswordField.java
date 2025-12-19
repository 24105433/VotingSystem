package ph.robleding.votingsystem.ui;

import java.io.Console;
import java.util.Scanner;

public class PasswordField {

    // Reuse ONE scanner
    private static final Scanner SCANNER = new Scanner(System.in);

    public static String readPassword(String prompt) {
        Console console = System.console();

        if (console != null) {
            char[] passwordChars = console.readPassword(prompt);
            return new String(passwordChars);
        } else {
            // IntelliJ / VS Code fallback
            System.out.print(prompt);
            return SCANNER.nextLine();
        }
    }
}
