package ru.nove.model.searchable;

import ru.nove.model.entities.Drink;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DrinkSearchable implements Searchable<Drink, String>{

    private List<Drink> drinks = new ArrayList<>();

    public DrinkSearchable(List<Drink> drinks){
        this.drinks = drinks;
    }

    public Collection<Drink> search(String value) {
        List<Drink> founds = new ArrayList<>();

        for(Drink drink:drinks){
            if(drink.getName().toLowerCase().contains(value)){
                founds.add(drink);
            }
        }
        return founds;
    }
}
