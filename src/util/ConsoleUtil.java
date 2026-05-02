package util;

public class ConsoleUtil {

    // ANSI Color Codes
    public static final String RESET   = "\u001B[0m";
    public static final String CYAN    = "\u001B[36m";
    public static final String GREEN   = "\u001B[32m";
    public static final String RED     = "\u001B[31m";
    public static final String PURPLE  = "\u001B[35m";
    public static final String YELLOW  = "\u001B[33m";
    public static final String BOLD    = "\u001B[1m";
    public static final String WHITE   = "\u001B[37m";

    private static final char TOP_LEFT     = '╔';
    private static final char TOP_RIGHT    = '╗';
    private static final char BOTTOM_LEFT  = '╚';
    private static final char BOTTOM_RIGHT = '╝';
    private static final char HORIZONTAL   = '═';
    private static final char VERTICAL     = '║';
    private static final char MID_LEFT     = '╠';
    private static final char MID_RIGHT    = '╣';

    public static void printTitle(String title) {
        int width = 60;
        String border = repeat(String.valueOf(HORIZONTAL), width - 2);
        System.out.println(CYAN + BOLD);
        System.out.println(TOP_LEFT + border + TOP_RIGHT);
        System.out.println(VERTICAL + center(title, width - 2) + VERTICAL);
        System.out.println(BOTTOM_LEFT + border + BOTTOM_RIGHT);
        System.out.print(RESET);
    }

    public static void printSubTitle(String title) {
        int width = 60;
        String border = repeat(String.valueOf(HORIZONTAL), width - 2);
        System.out.println(CYAN);
        System.out.println(MID_LEFT + border + MID_RIGHT);
        System.out.println(VERTICAL + center(title, width - 2) + VERTICAL);
        System.out.println(MID_LEFT + border + MID_RIGHT);
        System.out.print(RESET);
    }

    public static void printBox(String message, String color) {
        int width = message.length() + 4;
        String border = repeat(String.valueOf(HORIZONTAL), width - 2);
        System.out.println(color);
        System.out.println(TOP_LEFT + border + TOP_RIGHT);
        System.out.println(VERTICAL + " " + message + " " + VERTICAL);
        System.out.println(BOTTOM_LEFT + border + BOTTOM_RIGHT);
        System.out.print(RESET);
    }

    public static void success(String msg) {
        System.out.println(GREEN + BOLD + "  ✔  " + msg + RESET);
    }

    public static void error(String msg) {
        System.out.println(RED + BOLD + "  ✘  " + msg + RESET);
    }

    public static void info(String msg) {
        System.out.println(YELLOW + "  ℹ  " + msg + RESET);
    }

    public static void stat(String label, Object value) {
        System.out.println("  " + WHITE + label + ": " + PURPLE + BOLD + value + RESET);
    }

    public static void printSeparator() {
        System.out.println(CYAN + "  " + repeat(String.valueOf(HORIZONTAL), 56) + RESET);
    }

    public static void printMenuOption(int num, String option) {
        System.out.println("  " + CYAN + "[" + num + "]" + RESET + " " + option);
    }

    public static void printTableHeader(String... cols) {
        StringBuilder sb = new StringBuilder();
        sb.append(CYAN + BOLD + "  ");
        for (String col : cols) {
            sb.append(String.format("%-20s", col));
        }
        sb.append(RESET);
        System.out.println(sb);
        printSeparator();
    }

    public static void printTableRow(String... cols) {
        StringBuilder sb = new StringBuilder();
        sb.append("  ");
        for (String col : cols) {
            sb.append(String.format("%-20s", col));
        }
        System.out.println(sb);
    }

    private static String center(String text, int width) {
        if (text.length() >= width) return text;
        int pad = (width - text.length()) / 2;
        return repeat(" ", pad) + text + repeat(" ", width - pad - text.length());
    }

    private static String repeat(String s, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) sb.append(s);
        return sb.toString();
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
