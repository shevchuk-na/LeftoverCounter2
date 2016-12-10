package ru.nove.model.utils;

import ru.nove.model.entities.Drink;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SaveLoadUtil {
    private static final String ARCHIVE_FILE = "data/archive.save";

    public void saveArchive(List<Drink> archive) throws IOException{
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVE_FILE));
        oos.writeObject(archive);
        oos.flush();
        oos.close();
    }

    public List<Drink> loadArchive() throws IOException, ClassNotFoundException {
        if(checkSaveFile() == 1){
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVE_FILE));
            List<Drink> archive = (ArrayList<Drink>) ois.readObject();
            ois.close();
            return archive;
        }
        return null;
    }

    private int checkSaveFile() throws IOException {
        File file = new File(ARCHIVE_FILE);
        if(!file.exists()){
            File parent = new File(file.getParent());
            parent.mkdir();
            file.createNewFile();
            return 0;
        }
        return 1;
    }
}
