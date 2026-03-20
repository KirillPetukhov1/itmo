package src.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;

import src.baseabstractions.AbstractClientManager;
import src.baseabstractions.Command;
import src.baseabstractions.ProductClientManager;
import src.basecollection.CollectionManager;
import src.baseobjects.Product;
import src.console.CommandManager;
import src.workwithfiles.ConsoleFileManager;
import src.workwithfiles.FilesStack;

/**
 * Command to execute a script from a file.
 * Reads and executes commands sequentially from the specified file.
 *
 * @param <K> the type of keys used in the collection, must be Comparable
 * @param <V> the type of values stored in the collection, must extend Product
 */
public class ExecuteScriptCommand<K extends Comparable<K>, V extends Product> extends Command<K, V> {
    private HashMap<String, Command<?, ? extends Product>> commands;
    private ProductClientManager<V> productClientManager;

    /**
     * Constructs an ExecuteScriptCommand with the specified collection manager and
     * product client manager.
     *
     * @param collectionManager    the collection manager to operate on
     * @param productClientManager the product client manager for handling product
     *                             operations
     */
    public ExecuteScriptCommand(CollectionManager<K, V> collectionManager,
            ProductClientManager<V> productClientManager) {
        super(collectionManager);
        this.commands = CommandManager.getCommands();
        this.productClientManager = productClientManager;
    }

    /**
     * Executes the script from the specified file.
     * Handles nested script execution with stack to prevent infinite recursion.
     *
     * @param args command arguments (expects command name and file name)
     * @throws IllegalArgumentException if the number of arguments is incorrect
     */
    public void execute(String[] args) {
        if (args.length == 2) {
            String fileName = args[1];
            if (!FilesStack.getFilesStack().contains(fileName)) {
                FilesStack.addFile(fileName);
                try {
                    ConsoleFileManager readerWriter = new ConsoleFileManager(new Scanner(new File(fileName)));
                    if (productClientManager instanceof AbstractClientManager) {
                        ((AbstractClientManager) productClientManager).setReaderWriter(readerWriter);
                        try {
                            while (readerWriter.getScanner().hasNext()) {
                                String[] commandAndArgument = readerWriter.readCommandAndArgument();
                                String command = commandAndArgument[0];
                                if (commands.containsKey(command)) {
                                    if (command.equals("insert")) {
                                        K key = getCollectionManager().getKeyParser()
                                                .getParsedObject(commandAndArgument[1]);
                                        getCollectionManager().insert(key, productClientManager);
                                    } else if (command.equals("update")) {
                                        long id = Long.parseLong(commandAndArgument[1]);
                                        getCollectionManager().update(id, productClientManager);
                                    } else if (command.equals("remove_lower")) {
                                        getCollectionManager().removeLower(productClientManager);
                                    } else if (command.equals("replace_if_greater")) {
                                        K key = getCollectionManager().getKeyParser()
                                                .getParsedObject(commandAndArgument[1]);
                                        getCollectionManager().replaceIfGreater(key, productClientManager);
                                    } else {
                                        commands.get(command).execute(commandAndArgument);
                                    }
                                }
                            }
                        } catch (IllegalArgumentException | NullPointerException | NoSuchElementException e) {
                            System.out.println(e);
                        }
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("File not found.");
                } finally {
                    FilesStack.removeFile();
                }
            }
        } else
            throw new IllegalArgumentException("Number of arguments is wrong.");
    }

    /**
     * Returns a description of the command.
     *
     * @return "execute_script file_name : считать и исполнить скрипт из указанного
     *         файла. В скрипте содержатся команды в таком же виде, в котором их
     *         вводит пользователь в интерактивном режиме." - command description in
     *         Russian
     */
    public String getDescription() {
        return "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.";
    }
}