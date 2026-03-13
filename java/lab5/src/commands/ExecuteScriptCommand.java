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

public class ExecuteScriptCommand<K extends Comparable<K>, V extends Product> extends Command<K, V> {
    private HashMap<String, Command<?, ? extends Product>> commands;
    private ProductClientManager<V> productClientManager;

    public ExecuteScriptCommand(CollectionManager<K, V> collectionManager,
            ProductClientManager<V> productClientManager) {
        super(collectionManager);
        this.commands = CommandManager.getCommands();
        this.productClientManager = productClientManager;
    }

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
                        } catch (IllegalArgumentException | NullPointerException | NoSuchElementException ignored) {
                        }
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("Файл не найден. ");
                } finally {
                    FilesStack.removeFile();
                }
            }
        } else
            throw new IllegalArgumentException("Number of arguments is wrong.");
    }

    // protected void insert(String[] args, AbstractFileConsoleReader reader) {
    // K key = getCollectionManager().getKeyParser().getParsedObject(args[1]);
    // if (getCollectionManager().getProducts().containsKey(key)) {
    // System.out.println("Коллекция уже содержит элемент с таким ключом. Будет
    // произведена перезапись.");
    // }
    // reader.readProduct();
    // ProductBuilder<V> productBuilder = reader.getProductBuilder();

    // productBuilder = productBuilder.setCreationDate(LocalDateTime.now());

    // LongIdManager<Product> idManager = new LongIdManager<>();
    // productBuilder = productBuilder.setId(idManager.generateId());
    // Product product = productBuilder.create();
    // idManager.addId(product);

    // getCollectionManager().putProduct(key, product);
    // System.out.println("Коллекция успешно обновлена.");
    // }

    // protected void update(String[] args, AbstractFileConsoleReader reader) {

    // }

    // protected void removeLower(String[] args, AbstractFileConsoleReader reader) {

    // }

    // protected void replaceIfGreater(String[] args, AbstractFileConsoleReader
    // reader) {

    // }

    public String getDescription() {
        return "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.";
    }
}
