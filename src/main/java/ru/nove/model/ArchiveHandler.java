package ru.nove.model;

import ru.nove.model.entities.Drink;

import java.util.ArrayList;
import java.util.List;

public class ArchiveHandler {
    private List<Drink> archive;

    public ArchiveHandler(){
        this.archive = new ArrayList<>();
        archive.add(new Drink("Монтепульчано", 0, 125));
        archive.add(new Drink("Просекко", 0, 150));
        archive.add(new Drink("Водка", 0, 50));
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
