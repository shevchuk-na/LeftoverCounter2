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

    public void setArchive(List<Drink> archive) {
        this.archive = archive;
    }
}
