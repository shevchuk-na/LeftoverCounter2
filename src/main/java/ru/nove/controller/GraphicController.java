package ru.nove.controller;

import ru.nove.model.DrinkListModel;
import ru.nove.model.entities.Drink;
import ru.nove.view.GUI;

import java.util.List;

public class GraphicController {
    private GUI gui;
    private DrinkListModel model;
    private final int OK = 1;
    private final int ERROR = -1;

    public GraphicController() {
        gui = new GUI(this);
        model = new DrinkListModel(gui);
    }

    public void launch(){
        gui.createGui();
//        model.loadData();
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
//        if(model.saveData() == OK){
            model.exit();
//        }else {
//
//        }
    }

    public void viewHistory(int mode) {
        model.viewHistory(mode);

    }

    public void cancelLast() {
        model.deleteLast();
    }


    public List<Drink> getRegistry() {
        return model.getRegistry();
    }
}
