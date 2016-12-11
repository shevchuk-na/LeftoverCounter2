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

    void setArchive(List<Drink> archive) {
        this.archive = archive;
    }
}
