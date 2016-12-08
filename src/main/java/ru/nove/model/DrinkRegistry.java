package ru.nove.model;

import ru.nove.model.entities.Drink;

import java.util.ArrayList;

public class DrinkRegistry {
    private ArrayList<Drink> registry;

    public void addToRegistry(Drink drink){
        registry.add(new Drink(drink.getName(), 0, drink.getDefaultAmount()));
    }

    public Drink findDrink(String name){
        for(Drink drink:registry){
            if(drink.getName().equals(name)){
                return drink;
            }
        }
        return null;
    }
}
