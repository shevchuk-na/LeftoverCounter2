package ru.nove.model;

import ru.nove.model.entities.Drink;

import java.util.ArrayList;
import java.util.List;

public class ArchiveHandler {
    private List<Drink> archive;

    public ArchiveHandler(){
        this.archive = new ArrayList<>();
    }

    public void addToArchive(Drink drink){
        archive.add(drink);
    }

    public List<Drink> getArchive(){
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
