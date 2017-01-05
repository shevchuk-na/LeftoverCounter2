package ru.nove.view;

import javax.swing.*;
import java.awt.*;

class SellAddWindow {
    private GUI gui;
    private JFrame sellAddFrame;
    private JTextField nameField, saleAmount;
    private JButton saleButton;

    SellAddWindow(GUI gui, String drink, SellAddModes command){
        this.gui = gui;
        sellAddFrame = new JFrame();
        saleButton = new JButton();
        switch (command){
            case customSale:
                sellAddFrame.setTitle("Продать количество");
                saleButton.setText("Продать");
                break;
            case customAdd:
                sellAddFrame.setTitle("Добавить количество");
                saleButton.setText("Добавить");
                break;
        }
        JPanel salePanel = new JPanel();
        nameField = new JTextField(drink, 15);
        nameField.setEditable(false);
        nameField.setMargin(new Insets(3,2,3,2));
        nameField.setFont(nameField.getFont().deriveFont(Font.BOLD));
        saleAmount = new JTextField(5);
        saleAmount.setMargin(new Insets(3,2,3,2));
        saleButton.addActionListener(e -> performCustomAction(nameField.getText(), saleAmount.getText(), saleButton.getText()));
        salePanel.add(nameField);
        salePanel.add(saleAmount);
        salePanel.add(saleButton);
        sellAddFrame.getContentPane().add(salePanel);
        sellAddFrame.setSize(new Dimension(360, 80));
        sellAddFrame.setLocationRelativeTo(gui.getMainFrame());
        sellAddFrame.getRootPane().setDefaultButton(saleButton);
        sellAddFrame.setVisible(true);
        saleAmount.requestFocus();
    }

    void showSellAddWindow(String drink, SellAddModes command) {
        if(sellAddFrame != null){
            switch (command){
                case customSale:
                    sellAddFrame.setTitle("Продать количество");
                    saleButton.setText("Продать");
                    break;
                case customAdd:
                    sellAddFrame.setTitle("Добавить количество");
                    saleButton.setText("Добавить");
                    break;
            }
            nameField.setText(drink);
            saleAmount.setText("");
            saleAmount.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY), BorderFactory.createEmptyBorder(3,2,3,2)));
            sellAddFrame.setVisible(true);
            saleAmount.requestFocus();
        }
    }

    private void performCustomAction(String name, String amount, String command){
        try {
            if(amount.equals("")){
                saleAmount.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.RED), BorderFactory.createEmptyBorder(3,2,3,2)));
            } else {
                sellAddFrame.setVisible(false);
                switch(command){
                    case "Продать":
                        gui.getController().sellAmount(name, Integer.parseInt(amount));
                        break;
                    case "Добавить":
                        gui.getController().addAmount(name, Integer.parseInt(amount));
                        break;
                }
            }
        }catch (NumberFormatException ex){
            ex.printStackTrace();
            gui.showNumberFormatWarning();
        }
    }

    void close(){
        sellAddFrame.dispose();
    }
}
