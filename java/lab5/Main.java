
import java.util.Scanner;

import src.basecollection.CollectionManager;
import src.baseobjects.Product;
import src.commands.ExecuteScriptCommand;
import src.console.BaseConsoleClientManager;
import src.console.CommandManager;
import src.console.ConsoleManager;
import src.console.keyparsers.StringKeyParser;
import src.productcreation.LongIdManager;
import src.workwithfiles.BaseFileConsoleClientManager;
import src.workwithfiles.ProductXmlFileManager;

public class Main {
    public static void main(String[] args) {
        try {
            CollectionManager<String, Product> collectionManager = new CollectionManager<>();

            Scanner consoleScanner = new Scanner(System.in);

            ConsoleManager consoleManager = new ConsoleManager(consoleScanner);
            BaseConsoleClientManager baseConsoleClientManager = new BaseConsoleClientManager(consoleManager);
            collectionManager.setProductClientManager(baseConsoleClientManager);

            LongIdManager<Product> longIdManager = new LongIdManager<>();
            collectionManager.setIdManager(longIdManager);

            StringKeyParser stringKeyParser = new StringKeyParser();
            collectionManager.setKeyParser(stringKeyParser);

            String fileName = System.getenv("LAB5_FILENAME");
            ProductXmlFileManager<String, Product> productXmlFileManager = new ProductXmlFileManager<>(fileName);
            collectionManager.setFileManager(productXmlFileManager);
            collectionManager.setProducts(productXmlFileManager.load());

            CommandManager<String, Product> commandManager = new CommandManager<>(collectionManager, consoleScanner);
            BaseFileConsoleClientManager baseFileConsoleClientManager = new BaseFileConsoleClientManager(
                    consoleManager);
            CommandManager.addCommand("execute_script",
                    new ExecuteScriptCommand<String, Product>(collectionManager, baseFileConsoleClientManager));

            while (true) {
                while (commandManager.getWork()) {
                    commandManager.existCommand();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Something goes wrong. See you next time!");
        }
    }
}