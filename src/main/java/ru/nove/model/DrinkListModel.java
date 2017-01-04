package ru.nove.model;

import ru.nove.model.entities.Drink;
import ru.nove.model.utils.AutoSaveUtil;
import ru.nove.model.utils.SaveLoadUtil;
import ru.nove.view.GUI;
import ru.nove.view.HistoryModes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static ru.nove.model.entities.Drink.getCompByName;

public class DrinkListModel {
    private List<Drink> drinks;
    private List<Drink> salesThisSession;
    private ArchiveHandler archiveHandler;
    private HistoryHandler historyHandler;
    private GUI gui;
    private SaveLoadUtil saveLoad;
    private final int OK = 1;
    private final int ERROR = -1;

    public DrinkListModel(GUI gui) {
        drinks = new ArrayList<>();
        salesThisSession = new ArrayList<>();
        this.gui = gui;
        this.archiveHandler = new ArchiveHandler();
        this.historyHandler = new HistoryHandler(archiveHandler);
        this.saveLoad = new SaveLoadUtil();
        Thread autoSaveThread = new Thread(new AutoSaveUtil(archiveHandler, saveLoad, this.gui));
        autoSaveThread.setDaemon(true);
        autoSaveThread.start();

    }

    public void addDrink(String name, int amount, int defaultAmount) {
        Drink newDrink = null;
        for (Drink drink : archiveHandler.getArchive()) {
            if (drink.getName().equals(name)) {
                if (drink.getAmount() != 0) {
                    gui.showDuplicateWarning();
                    return;
                }
                newDrink = drink;
                break;
            }
        }
        if (newDrink == null) {
            newDrink = new Drink(name, amount, defaultAmount);
            archiveHandler.addToArchive(newDrink);
        } else {
            newDrink.addAmount(amount);
        }
        drinks.add(newDrink);
        salesThisSession.add(newDrink);
        gui.enableCancelButton(true);
        gui.addPosition(newDrink);
    }

    public void sellDrink(String name, int amount){
        int index = getDrinkIndex(name);
        Drink drink = drinks.get(index);
        if(amount == 0){
            amount = drink.getDefaultAmount();
        }
        drink.addSale(amount);
        salesThisSession.add(drink);
        gui.enableCancelButton(true);
        gui.showSaleInfo(drink, amount);
        if(drink.getAmount() == 0){
            gui.removePosition(drink);
            drinks.remove(drink);
        } else {
            gui.updatePosition(drink);
        }
    }

    public void sellDrink(String name){
        sellDrink(name, 0);
    }

    private int getDrinkIndex(String name){
        for(Drink drink:drinks){
            if(drink.getName().equals(name)){
                return drinks.indexOf(drink);
            }
        }
        return ERROR;
    }

    public void addAmount(String name, int amount){
        Drink drink = drinks.get(getDrinkIndex(name));
        drink.addAmount(amount);
        salesThisSession.add(drink);
        gui.enableCancelButton(true);
        gui.showAddInfo(drink, amount);
        if(drink.getAmount() == 0){
            gui.removePosition(drink);
            drinks.remove(drink);
        } else {
            gui.updatePosition(drink);
        }
    }

    public void exit() {
        gui.closeAllFrames();
    }

    public void viewHistory(HistoryModes mode) {
        List<String> history = historyHandler.formHistory(mode);
        gui.displayHistory(history);
    }

    public void loadData() {
        try {
            List<Drink> loadedArchive = saveLoad.loadArchive();
            if(loadedArchive != null) {
                archiveHandler.setArchive(loadedArchive);
                for(Drink drink:archiveHandler.getArchive()){
                    if(drink.getAmount() != 0){
                        drinks.add(drink);
                    }
                }
                gui.showDrinks(drinks);
                gui.showLoadedInfo();
            } else {
                gui.showNewSaveFileInfo();
            }
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
            gui.showLoadError();
        }
    }

    public int saveData() {
        try {
            saveLoad.saveArchive(archiveHandler.getArchive());
        } catch (IOException e) {
            e.printStackTrace();
            gui.showSaveError();
            return ERROR;
        }
        return OK;
    }

    public void deleteLast() {
        if(salesThisSession.size() != 0){
            int lastSessionSaleIndex = salesThisSession.size()-1;
            int lastDrinkIndex = archiveHandler.getArchive().indexOf(salesThisSession.get(lastSessionSaleIndex));
            Drink lastDrink = archiveHandler.getArchive().get(lastDrinkIndex);
            if(lastDrink.getAmountHistory().size() == 1){                   //только что добавили
                gui.removePosition(lastDrink);
                archiveHandler.removeFromArchive(lastDrink);
            } else {
                int lastAmount = lastDrink.getAmountHistory().get(lastDrink.getAmountHistory().size() - 1);
                int amountBeforeLast = lastDrink.getAmountHistory().get(lastDrink.getAmountHistory().size() - 2);
                if (amountBeforeLast == 0) {                              //реактивация позиции
                    gui.removePosition(lastDrink);
                    lastDrink.removeSale();
                    drinks.remove(lastDrink);
                } else {
                    if (lastAmount > amountBeforeLast) {                          //добавили
                        lastDrink.removeSale();
                        if (lastAmount == 0) {
                            gui.addPosition(lastDrink);
                        } else if (amountBeforeLast < 0 && lastAmount > 0) {
                            gui.switchPosition(lastDrink);
                        } else {                                                //просто добавление
                            gui.updatePosition(lastDrink);
                        }
                    } else {                                                    //убрали
                        lastDrink.removeSale();
                        if (lastAmount == 0) {                                    //убрали последнее
                            drinks.add(lastDrink);
                            gui.addPosition(lastDrink);
                        } else if (amountBeforeLast > 0 && lastAmount < 0) {
                            gui.switchPosition(lastDrink);
                        } else {                                                //просто убрали
                            gui.updatePosition(lastDrink);
                        }
                    }
                }
            }
            salesThisSession.remove(lastSessionSaleIndex);
            if(salesThisSession.isEmpty()){
                gui.enableCancelButton(false);
            }
            gui.showCancelInfo();
        }
    }

    public void prepareGui() {
        gui.enableCancelButton(false);
    }

    public List<Drink> getArchive() {
        List<Drink> drinks = archiveHandler.getArchive();
        drinks.sort(getCompByName());
        return drinks;
    }

    public List<Drink> getActualArchive() {
        List<Drink> drinks = archiveHandler.getActualArchive();
        drinks.sort(getCompByName());
        return drinks;
    }

    public int checkRegistry(String name) {
        for(Drink drink:archiveHandler.getArchive()){
            if(drink.getName().equals(name)){
                return drink.getDefaultAmount();
            }
        }
        return 0;
    }

    public void editArchiveItem(Drink drink, String name, int defaultAmount) {
        archiveHandler.editName(drink, name);
        archiveHandler.editDefaultAmount(drink, defaultAmount);
        gui.showDrinks(drinks);
    }

    public void makeItemObsolete(Drink drink) {
        if(drink.getAmount() == 0){
            drink.setObsolete(true);
        }else{
            gui.showNotEmptyWarning();
        }
    }


}

