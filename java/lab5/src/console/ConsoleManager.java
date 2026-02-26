package src.console;

import java.util.Scanner;

import src.baseabstractions.ReaderWriter;

public class ConsoleManager implements ReaderWriter {

    public Integer readInt() {
        Scanner scanner = new Scanner(System.in);
        Integer result = Integer.valueOf(scanner.nextLine().trim());
        scanner.close();
        return result;
    }

    public Long readLong() {
        Scanner scanner = new Scanner(System.in);
        long result = Long.valueOf(scanner.nextLine().trim());
        scanner.close();
        return result;
    }

    public Double readDouble() {
        Scanner scanner = new Scanner(System.in);
        Double result = Double.valueOf(scanner.nextLine().trim());
        scanner.close();
        return result;
    }

    public String readLine() {
        Scanner scanner = new Scanner(System.in);
        String result = scanner.nextLine().trim();
        scanner.close();
        return result;
    }

    public void write(String text) {
        System.out.print(text);
    }
}
