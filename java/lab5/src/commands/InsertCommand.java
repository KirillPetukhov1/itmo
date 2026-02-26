package src.commands;

import src.baseabstractions.Command;
import src.basecollection.CollectionManager;
import src.baseobjects.Product;

public class InsertCommand<K, V extends Product> extends Command<K, V> {

    public InsertCommand(CollectionManager<K, V> collectionManager) {
        super(collectionManager);
    }

    public void execute(String[] args) {
        if (args.length == 2) {
            // getCollectionManager().help();
            // TODO
            try {
                MusicBand musicBand = cm.getClientManager().getMusicBand();
                boolean flag = true;
                for (MusicBand musicBand1 : cm.getMusicBands()) {
                    if (musicBand1.equals(musicBand)) {
                        flag = false;
                    }
                }
                if (flag) {
                    cm.add(musicBand);
                    System.out.println("Элемент успешно добавлен в коллекцию.");
                } else {
                    System.out.println("Введеный элемент уже есть в коллекции. ");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Введены неверные аргументы. Попробуйте еще раз");
            }
        } else
            throw new IllegalArgumentException("Number of arguments is wrong.");
    }

    public String getDescription() {
        return "insert null {element} : добавить новый элемент с заданным ключом";
    }
}
