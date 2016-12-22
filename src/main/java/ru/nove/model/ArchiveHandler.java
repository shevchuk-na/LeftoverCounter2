package ru.nove.model;

import ru.nove.model.entities.Drink;

import java.util.ArrayList;
import java.util.List;

public class ArchiveHandler {
    private List<Drink> archive;

    ArchiveHandler(){
        this.archive = new ArrayList<>();
    }

    void addToArchive(Drink drink){
        archive.add(drink);
    }

    void removeFromArchive(Drink drink){
        archive.remove(drink);
    }

    public List<Drink> getArchive(){
        return archive;
    }

    List<Drink> getActualArchive(){
        List<Drink> actualArchive = new ArrayList<>();
        for(Drink drink:archive){
            if(!drink.isObsolete()){
                actualArchive.add(drink);
            }
        }
        return actualArchive;
    }

    void setArchive(List<Drink> archive) {
        this.archive = archive;
    }

    void editName(Drink drink, String name){
        drink.setName(name);
    }

    void editDefaultAmount(Drink drink, int defaultAmount){
        drink.setDefaultAmount(defaultAmount);
    }
}
