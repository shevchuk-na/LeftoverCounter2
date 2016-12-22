package ru.nove.view;

import javax.swing.*;
import java.awt.*;
import java.util.List;

class HistoryWindow {
    private GUI gui;
    private JFrame historyFrame;
    private JTextArea historyLog;
    private JRadioButton drinkOrderRadioButton;

    HistoryWindow(GUI gui){
        this.gui = gui;
        historyFrame = new JFrame("История операций");
        historyLog = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(historyLog, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        Box buttonBox = new Box(BoxLayout.X_AXIS);
        drinkOrderRadioButton = new JRadioButton("По наименованию");
        JRadioButton chronoOrderRadioButton = new JRadioButton("По хронологии");
        chronoOrderRadioButton.setSelected(true);
        JButton closeButton = new JButton("Выход");
        closeButton.addActionListener(e -> historyFrame.setVisible(false));
        JButton updateButton = new JButton("Обновить");
        updateButton.addActionListener(e -> updateHistory());
        ButtonGroup historyOrder = new ButtonGroup();
        historyOrder.add(drinkOrderRadioButton);
        historyOrder.add(chronoOrderRadioButton);
        buttonBox.add(updateButton);
        buttonBox.add(drinkOrderRadioButton);
        buttonBox.add(chronoOrderRadioButton);
        buttonBox.add(closeButton);
        historyFrame.add(scrollPane, BorderLayout.CENTER);
        historyFrame.add(buttonBox, BorderLayout.SOUTH);
        historyFrame.pack();
        historyFrame.setLocationRelativeTo(gui.getMainFrame());
        historyFrame.setVisible(true);
    }

    void showHistoryWindow(){
        if(historyFrame != null) {
            historyFrame.setVisible(true);
            updateHistory();
        }
    }

    private void updateHistory() {
        if(drinkOrderRadioButton.isSelected()){
            gui.getController().viewHistory(HistoryModes.alphabetical);
        }else{
            gui.getController().viewHistory(HistoryModes.chronological);
        }
    }

    void displayHistory(List<String> history) {
        historyLog.setText("");
        for(String line:history){
            historyLog.append(line+"\n");
        }
    }

    void close(){
        historyFrame.dispose();
    }

}
