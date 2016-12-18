package ru.nove.view;

import ru.nove.model.searchable.DrinkSearchable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class InputWindow {

    private GUI gui;
    private JFrame inputFrame;
    private JTextField amount, defaultAmount;
    private AutocompleteJComboBox name;
    private JButton addButton;

    InputWindow(GUI gui){
        this.gui = gui;
        inputFrame = new JFrame("Введите данные");
        JPanel panel = new JPanel();
        amount = new JTextField(6);
        amount.setMargin(new Insets(3, 2, 3, 2));
        JLabel defAmoLabel = new JLabel("Размер порции по умолчанию");
        defaultAmount = new JTextField(5);
        defaultAmount.setMargin(new Insets(3, 2, 3, 2));
        name = new AutocompleteJComboBox(new DrinkSearchable(gui.getController().getActualArchive()));
        name.addItemListener(e -> checkDefaultAmount());
        name.addItemListener(e -> checkEntry());
        amount.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                checkEntry();
            }
        });
        defaultAmount.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                checkEntry();
            }
        });
        addButton = new JButton("Добавить");
        addButton.setEnabled(false);
        addButton.addActionListener((e) -> addData());
        panel.add(name);
        panel.add(amount);
        panel.add(defAmoLabel);
        panel.add(defaultAmount);
        panel.add(addButton);
        inputFrame.setSize(320, 145);
        inputFrame.getContentPane().add(panel);
        inputFrame.setLocationRelativeTo(gui.getMainFrame());
        inputFrame.setVisible(true);
        name.requestFocus();
    }

    void showInputWindow() {
        if (inputFrame != null) {
            inputFrame.setVisible(true);
            name.setSelectedItem(null);
            amount.setText("");
            defaultAmount.setText("");
            addButton.setEnabled(false);
            name.requestFocus();
        }
    }

    private void addData() {
        inputFrame.setVisible(false);
        try {
            gui.getController().addDrink(((String) name.getSelectedItem()), Integer.parseInt(amount.getText()), Integer.parseInt(defaultAmount.getText()));
        }catch (NumberFormatException e){
            gui.showNumberFormatWarning();
        }
    }

    private void checkEntry(){
        if(name.getSelectedItem() != null) {
            if (!name.getSelectedItem().toString().equals("") && !amount.getText().equals("") && !defaultAmount.getText().equals("")) {
                addButton.setEnabled(true);
                return;
            }
        }
        addButton.setEnabled(false);
    }

    private void checkDefaultAmount() {
        if(name.getSelectedItem() != null) {
            String drink = name.getSelectedItem().toString();
            int defAmount = gui.getController().checkRegistry(drink);
            if (defAmount != 0) {
                defaultAmount.setText(String.valueOf(defAmount));
            }
        }
    }

    void close(){
        inputFrame.dispose();
    }
}
