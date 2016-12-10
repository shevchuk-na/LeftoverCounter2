package ru.nove.model.utils;

import ru.nove.model.ArchiveHandler;
import ru.nove.view.GUI;

import java.io.IOException;

public class AutoSaveUtil implements Runnable{
    private SaveLoadUtil saver;
    private ArchiveHandler archiver;
    private GUI gui;
    private boolean running;

    public AutoSaveUtil(ArchiveHandler archiver, SaveLoadUtil saver, GUI gui){
        this.archiver = archiver;
        this.saver = saver;
        this.gui = gui;
        running = true;
    }

    @Override
    public void run() {
        try{
            while(running){
                Thread.sleep(300000);
                saver.saveArchive(archiver.getArchive());
                gui.showAutoSaveInfo();
            }
        } catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
    }
}
