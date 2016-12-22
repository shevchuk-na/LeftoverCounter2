package ru.nove.model;

import ru.nove.model.entities.Drink;
import ru.nove.model.utils.DateUtil;
import ru.nove.view.HistoryModes;

import java.util.ArrayList;
import java.util.List;

import static ru.nove.model.entities.Drink.getCompByName;

class HistoryHandler {
    private ArchiveHandler archiveHandler;

    HistoryHandler(ArchiveHandler archiveHandler){
        this.archiveHandler = archiveHandler;
    }

    List<String> formHistory(HistoryModes mode) {
        List<String> history = new ArrayList<>();
        List<Drink> allDrinks = new ArrayList<>();
        allDrinks.addAll(archiveHandler.getArchive());

        switch(mode){
            case alphabetical:
                allDrinks.sort(getCompByName());
                for(Drink drink: allDrinks){
                    for (int i = 0; i < drink.getAmountHistory().size(); i++) {
                        history.add(makeHistoryLine(drink, i));
                    }
                }
                break;
            case chronological:
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
        String historyLine = DateUtil.formatTime(drink.getTsHistory().get(i)) + " - " + drink.getName();
        if(i == 0) {
            if(drink.getAmountHistory().get(i) < 0){
                historyLine += " ";
            }else{
                historyLine += " +";
            }
            historyLine += drink.getAmountHistory().get(i);
        } else {
            if (drink.getAmountHistory().get(i) > drink.getAmountHistory().get(i - 1)) {
                historyLine += " +";
            } else {
                historyLine += " ";
            }
            historyLine += (drink.getAmountHistory().get(i) - drink.getAmountHistory().get(i - 1));
        }
        historyLine += ": " + drink.getAmountHistory().get(i);
        return historyLine;
    }
}
