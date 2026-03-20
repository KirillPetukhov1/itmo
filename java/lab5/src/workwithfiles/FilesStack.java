package src.workwithfiles;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Utility class for managing a stack of files during script execution.
 * Used to track nested script calls and prevent infinite recursion.
 */
public class FilesStack {
    /**
     * Stack of files currently being executed.
     * Stores filenames in LIFO order to track nested script calls.
     */
    private static final Deque<String> filesStack = new ArrayDeque<>();

    /**
     * Default constructor for FilesStack.
     */
    public FilesStack() {
    }

    /**
     * Returns the current stack of files.
     *
     * @return Deque containing filenames of scripts currently being executed
     */
    public static Deque<String> getFilesStack() {
        return filesStack;
    }

    /**
     * Adds a new file to the top of the stack.
     * Called when starting execution of a script file.
     *
     * @param filename the name of the file being added to the stack
     */
    public static void addFile(String filename) {
        filesStack.push(filename);
    }

    /**
     * Removes the top file from the stack.
     * Called when finishing execution of a script file.
     */
    public static void removeFile() {
        filesStack.pop();
    }
}