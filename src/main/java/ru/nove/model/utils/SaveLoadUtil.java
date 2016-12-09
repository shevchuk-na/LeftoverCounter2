package ru.nove.model.utils;

import ru.nove.model.entities.Drink;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SaveLoadUtil {
    private static final String SAVE_FILE = "data/data.save";
    private static final String ARCHIVE_FILE = "data/archive.save";

    public void saveData(List<Drink> drinks) throws IOException {
        checkSaveFile(SAVE_FILE);
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE));
        oos.writeObject(drinks);
        oos.flush();
        oos.close();
    }

    public List<Drink> loadData() throws IOException, ClassNotFoundException {
        if(checkSaveFile(SAVE_FILE) == 1) {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE));
            List<Drink> drinks = (ArrayList<Drink>) ois.readObject();
            ois.close();
            return drinks;
        }
        return null;
    }

    public void saveArchive(List<Drink> archive) throws IOException{
        checkSaveFile(ARCHIVE_FILE);
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVE_FILE));
        oos.writeObject(archive);
        oos.flush();
        oos.close();
    }

    public List<Drink> loadArchive() throws IOException, ClassNotFoundException {
        if(checkSaveFile(ARCHIVE_FILE) == 1){
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVE_FILE));
            List<Drink> archive = (ArrayList<Drink>) ois.readObject();
            ois.close();
            return archive;
        }
        return null;
    }

    private int checkSaveFile(String path) throws IOException {
        File file;
        switch(path){
            case SAVE_FILE:
                file = new File(SAVE_FILE);
                break;
            case ARCHIVE_FILE:
                file = new File(ARCHIVE_FILE);
                break;
            default:
                return -1;
        }
        if(!file.exists()){
            File parent = new File(file.getParent());
            parent.mkdir();
            file.createNewFile();
            return 0;
        }
        return 1;


    }
}
