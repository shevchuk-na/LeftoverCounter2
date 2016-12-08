package ru.nove.model;

import ru.nove.model.entities.Drink;

import java.util.ArrayList;

public class ArchiveHandler {
    private ArrayList<Drink> archive;

    public ArchiveHandler(){
        this.archive = new ArrayList<>();
    }

    public void addToArchive(Drink drink){
        archive.add(drink);
    }

    public ArrayList<Drink> getArchive(){
        return archive;
    }

//    public void setArchive(ArrayList<Drink> archive){
//        this.archive = archive;
//    }

    public Drink getLastAndRemove() {
        Drink lastDrink = archive.get(archive.size() - 1);
        archive.remove(lastDrink);
        return lastDrink;
    }
}
