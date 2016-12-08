package ru.nove.model;

import ru.nove.model.entities.Drink;
import ru.nove.model.utils.DateUtil;

import java.util.ArrayList;
import java.util.Comparator;

public class HistoryHandler {
    private DrinkListModel model;
    private ArchiveHandler archiveHandler;

    public HistoryHandler(DrinkListModel model, ArchiveHandler archiveHandeler){
        this.model = model;
        this.archiveHandler = archiveHandeler;
    }

    public ArrayList<String> formHistory(int mode) {
        ArrayList<String> history = new ArrayList<>();
        ArrayList<Drink> allDrinks = new ArrayList<>();
        allDrinks.addAll(archiveHandler.getArchive());
        allDrinks.addAll(model.getDrinks());
        switch(mode){
            case 1:
                allDrinks.sort(getCompByAddedDate());
                for(Drink drink: allDrinks){
                    for (int i = 0; i < drink.getAmountHistory().size(); i++) {
                        history.add(makeHistoryLine(drink, i));
                    }
                }
                break;
            case 2:
                ArrayList<Long> tsHistory = new ArrayList<>();
                for(Drink drink: allDrinks){
                    for(int i = 0; i < drink.getAmountHistory().size(); i++){
                        long ts = drink.getTsHistory().get(i);
                        String historyLine = makeHistoryLine(drink, i);
                        if (history.isEmpty() || ts > tsHistory.get(tsHistory.size() - 1)) {
                            history.add(historyLine);
                            tsHistory.add(ts);
                        } else {
                            for (int j = 0; j < history.size(); j++) {
                                if (ts < tsHistory.get(j)) {
                                    tsHistory.add(j, ts);
                                    history.add(j, historyLine);
                                    break;
                                }
                            }
                        }
                    }
                }
                break;
        }
        return history;
    }

    private String makeHistoryLine(Drink drink, int i) {
        if(i == 0 || (drink.getAmountHistory().get(i) > drink.getAmountHistory().get(i-1))){
            return DateUtil.formatTime(drink.getTsHistory().get(i)) + " - " + drink.getName() +
                    " +" + drink.getAmountHistory().get(i) + ": " + drink.getAmountHistory().get(i);
        } else{
            return DateUtil.formatTime(drink.getTsHistory().get(i)) + " - " + drink.getName() +
                    " " + (drink.getAmountHistory().get(i) - drink.getAmountHistory().get(i - 1)) + ": " + drink.getAmountHistory().get(i);
        }
    }

    private static Comparator<Drink> getCompByAddedDate(){
        return (d1, d2) ->
                d1.getAmountHistory().get(0).compareTo(d2.getAmountHistory().get(0));
    }
}
