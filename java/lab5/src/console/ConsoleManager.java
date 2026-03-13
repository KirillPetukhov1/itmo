package src.console;

import java.util.Scanner;

import src.baseabstractions.ReaderWriter;

public class ConsoleManager implements ReaderWriter {
    private final Scanner scanner;

    public ConsoleManager(Scanner scanner) {
        this.scanner = scanner;
    }

    public Scanner getScanner() {
        return this.scanner;
    }

    public Integer readInt() {
        Integer result = Integer.valueOf(scanner.nextLine().trim());
        return result;
    }

    public Long readLong() {
        long result = Long.valueOf(scanner.nextLine().trim());
        return result;
    }

    public Double readDouble() {
        Double result = Double.valueOf(scanner.nextLine().trim());
        return result;
    }

    public String readLine() {
        String result = scanner.nextLine().trim();
        return result;
    }

    public void write(String text) {
        System.out.print(text);
    }
}
