package ru.nove.model;

import ru.nove.model.entities.Drink;
import ru.nove.model.utils.SaveLoadUtil;
import ru.nove.view.GUI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        this.archiveHandler = new ArchiveHandler();
        this.historyHandler = new HistoryHandler(this, archiveHandler);
        this.gui = gui;
    }

    public void setDrinks(List<Drink> drinks) {
        this.drinks = drinks;
    }

    public List<Drink> getDrinks() {
        return drinks;
    }

    public void addDrink(String name, int amount, int defaultAmount){
        Drink newDrink = new Drink(name, amount, defaultAmount);
        if(checkDuplicateDrink(newDrink) == 1) {
            drinks.add(newDrink);
            archiveHandler.addToArchive(newDrink);
            salesThisSession.add(newDrink);
            gui.enableCancelButton(true);
            gui.addPosition(newDrink);
        } else {
            gui.showDuplicateWarning();
        }
    }

    private int checkDuplicateDrink(Drink newDrink) {
        for(Drink drink:drinks){
            if(drink.getName().toLowerCase().equals(newDrink.getName().toLowerCase())){
                return -1;
            }
        }
        return 1;
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
        if(drink.getAmount() == 0){
            gui.removePosition(drink.getName());
            drinks.remove(drink);
        } else {
            gui.updateAmount(drinks.get(index).getName(), drinks.get(index).getAmount());
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
        return -1;
    }

    public void exit() {
        gui.closeAllFrames();
    }

    public void viewHistory(int mode) {
        ArrayList<String> history = historyHandler.formHistory(mode);
        gui.displayHistory(history);
    }

    public void loadData() {
        saveLoad = new SaveLoadUtil();
        try {
            List<Drink> loaded = saveLoad.loadData();
            if(loaded != null){
                setDrinks(loaded);
                gui.showLoadedDrinks(drinks);
                gui.showLoadedInfo();
            } else {
                gui.showNewSaveFileInfo();
            }
            List<Drink> loadedArchive = saveLoad.loadArchive();
            if(loadedArchive != null) {
//                archiveHandler.setArchive(loadedArchive);
            }
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
            gui.showLoadError();
        }
    }

    public int saveData() {
        try {
            saveLoad.saveData(drinks);
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
            int indexOfDrinkLastSold = drinks.indexOf(salesThisSession.get(lastSessionSaleIndex));
            if(salesThisSession.get(lastSessionSaleIndex).getAmountHistory().size() == 1){
                gui.removePosition(drinks.get(indexOfDrinkLastSold).getName());
                drinks.remove(indexOfDrinkLastSold);
            }else{
                if(indexOfDrinkLastSold == -1){
                    Drink removed = archiveHandler.getLastAndRemove();
                    drinks.add(removed);
                    drinks.get(drinks.indexOf(removed)).removeSale();
                    gui.addPosition(drinks.get(drinks.indexOf(removed)));
                } else {
                    drinks.get(indexOfDrinkLastSold).removeSale();
                    gui.updateAmount(drinks.get(indexOfDrinkLastSold).getName(), drinks.get(indexOfDrinkLastSold).getAmount());
                }
            }
            salesThisSession.remove(lastSessionSaleIndex);
            if(salesThisSession.isEmpty()){
                gui.enableCancelButton(false);
            }
        }
    }

    private void drinkSold(Drink drink){

    }

    public void prepareGui() {
        gui.enableCancelButton(false);
    }

    public List<Drink> getRegistry() {
        return archiveHandler.getArchive();
    }

    public int checkregistry(String name) {
        for(Drink drink:archiveHandler.getArchive()){
            if(drink.getName().equals(name)){
                return drink.getDefaultAmount();
            }
        }
        return 0;
    }
}

