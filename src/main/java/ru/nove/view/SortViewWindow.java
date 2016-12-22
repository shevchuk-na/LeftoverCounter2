package ru.nove.view;

import javax.swing.*;
import java.awt.*;

class SortViewWindow {

    @SuppressWarnings("FieldCanBeLocal")
    private GUI gui;
    private JFrame sortViewFrame;
    private static final String ALPHABETICAL = "alphabetical";
    private static final String AVAILABLE = "available";

    SortViewWindow(GUI gui){
        this.gui = gui;
        sortViewFrame = new JFrame("Вид");
        JPanel filterPanel = new JPanel();
        ButtonGroup viewGroup = new ButtonGroup();
        JRadioButton alphabetView = new JRadioButton("По алфавиту");
        alphabetView.setSelected(true);
        JRadioButton chronoView = new JRadioButton("По количеству");
        JButton applyViewButton = new JButton("Применить");
        applyViewButton.addActionListener(e -> {
            if(alphabetView.isSelected()){
                gui.modifyView(SortViewModes.alphabetical);
            }else if(chronoView.isSelected()){
                gui.modifyView(SortViewModes.available);
            }
            sortViewFrame.setVisible(false);
        });
        viewGroup.add(alphabetView);
        viewGroup.add(chronoView);
        filterPanel.add(alphabetView);
        filterPanel.add(chronoView);
        filterPanel.add(applyViewButton);
        filterPanel.setPreferredSize(new Dimension(300, 70));

        sortViewFrame.getContentPane().add(filterPanel);
        sortViewFrame.pack();
        sortViewFrame.setLocationRelativeTo(this.gui.getMainFrame());
        sortViewFrame.setVisible(true);
    }

    void showSortViewWindow() {
        if(sortViewFrame!= null) {
            sortViewFrame.setVisible(true);
        }
    }

    void close(){
        sortViewFrame.dispose();
    }
}
