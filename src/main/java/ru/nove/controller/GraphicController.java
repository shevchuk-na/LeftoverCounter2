package ru.nove.controller;

import ru.nove.model.DrinkListModel;
import ru.nove.model.entities.Drink;
import ru.nove.view.GUI;
import ru.nove.view.HistoryModes;

import java.util.List;

public class GraphicController {
    private GUI gui;
    private DrinkListModel model;
    @SuppressWarnings("FieldCanBeLocal")
    private final int OK = 1;
    @SuppressWarnings("unused")
    private final int ERROR = -1;

    public GraphicController() {
        gui = new GUI(this);
        model = new DrinkListModel(gui);
    }

    public void launch(){
        gui.createGui();
        model.loadData();
        model.prepareGui();
    }

    public void addDrink(String name, int amount, int defaultAmount) {
        model.addDrink(name, amount, defaultAmount);

    }

    public void sellAmount(String drink) {
        model.sellDrink(drink);
    }

    public void sellAmount(String drink, int amount) {
        model.sellDrink(drink, amount);
    }



    public void exit() {
        if(model.saveData() == OK){
            model.exit();
        } else {
            gui.showSaveError();
        }
    }

    public void viewHistory(HistoryModes mode) {
        model.viewHistory(mode);

    }

    public void cancelLast() {
        model.deleteLast();
    }

    public int checkRegistry(String drink) {
        return model.checkRegistry(drink);
    }

    public void addAmount(String drink, int amount) {
        model.addAmount(drink, amount);
    }

    public void editArchiveItem(Drink drink, String name, int defaultAmount) {
        model.editArchiveItem(drink, name, defaultAmount);
    }

    public void makeItemObsolete(Drink drink) {
        model.makeItemObsolete(drink);
    }

    public List<Drink> getActualArchive() {
        return model.getActualArchive();
    }
}
