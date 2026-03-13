package src.workwithfiles;

import java.util.ArrayDeque;
import java.util.Deque;

public class FilesStack {
    /**
     * Stack of files.
     */
    private static final Deque<String> filesStack = new ArrayDeque<>();

    /**
     * Constructor for class objects.
     */
    public FilesStack() {
    }

    /**
     * @return filesStack
     */
    public static Deque<String> getFilesStack() {
        return filesStack;
    }

    /**
     * Adds new file to stack.
     * 
     * @param filename of new file
     */
    public static void addFile(String filename) {
        filesStack.push(filename);
    }

    /**
     * Deletes file from stack.
     */
    public static void removeFile() {
        filesStack.pop();
    }
}
