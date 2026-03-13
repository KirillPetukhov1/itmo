package src.baseabstractions;

import java.util.Scanner;

public interface ReaderWriter {
    Scanner getScanner();

    Integer readInt();

    Long readLong();

    Double readDouble();

    String readLine();

    void write(String text);
}
