package ru.nove.view;

import ru.nove.model.entities.Drink;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class InventoryWindow {
    private GUI gui;
    private JFrame inventoryFrame;
    private Box mainBox;
    private List<Box> boxArray;
    private JButton startButton, finishButton;

    InventoryWindow(GUI gui) {
        this.gui = gui;

        boxArray = new ArrayList<>();
        inventoryFrame = new JFrame("Инвентаризация");
        JPanel controlPanel = new JPanel();
        JPasswordField passwordField = new JPasswordField(10);
        JButton submitPassword = new JButton("ОК");
        submitPassword.addActionListener(e -> {
            if(checkPassword(passwordField.getPassword())) {
                startInventory();
            }
        });
        startButton = new JButton("Начать");
        startButton.setEnabled(false);
        finishButton = new JButton("Завершить");
        finishButton.setEnabled(false);

        controlPanel.add(passwordField);
        controlPanel.add(submitPassword);
        controlPanel.add(startButton);
        controlPanel.add(finishButton);

        mainBox = new Box(BoxLayout.Y_AXIS);

        JPanel mainPanel = new JPanel();
        mainPanel.add(mainBox, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(mainPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(500, 500));

        JTextField searchField = new JTextField(20);

        inventoryFrame.getContentPane().add(controlPanel, BorderLayout.NORTH);
        inventoryFrame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        inventoryFrame.getContentPane().add(searchField, BorderLayout.SOUTH);
        inventoryFrame.pack();
        inventoryFrame.setLocationRelativeTo(gui.getMainFrame());
        inventoryFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        inventoryFrame.setVisible(true);
    }

    private void startInventory() {
        startButton.setEnabled(true);
        startButton.addActionListener(e -> fillList());
        finishButton.setEnabled(true);
        finishButton.addActionListener(e -> parseValues());
    }

    private boolean checkPassword(char[] password) {
        boolean isCorrect;
        char[] correctPassword = {'a','d','m','i','n'};
        isCorrect = password.length == correctPassword.length && Arrays.equals(password, correctPassword);
        Arrays.fill(correctPassword, '0');
        return isCorrect;
    }

    private void fillList(){
        List<Drink> archive = new ArrayList<>();
        archive.addAll(gui.getController().getActualArchive());
        archive.sort(Drink.getCompByNameEmptyLast());
        for(Drink drink: archive){
            createBox(drink);
        }
        inventoryFrame.revalidate();
        inventoryFrame.repaint();
    }

    private void createBox(Drink drink) {
        Box rowBox = new Box(BoxLayout.X_AXIS);
        JTextField nameField = new JTextField(drink.getName(), 18);
        nameField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2,2,2,0),nameField.getBorder()));
        nameField.setFont(nameField.getFont().deriveFont(Font.BOLD));
        JTextField amountField = new JTextField(String.valueOf(drink.getAmount()),5);
        amountField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2,2,2,0),amountField.getBorder()));
        amountField.setHorizontalAlignment(JTextField.CENTER);
        amountField.setFont(amountField.getFont().deriveFont(Font.BOLD));
        JTextField newAmountField = new JTextField(5);
        JTextField delta = new JTextField(5);
        delta.setHorizontalAlignment(JTextField.CENTER);
        newAmountField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                try {
                    if (!newAmountField.getText().equals("")) {
                        newAmountField.setBackground(Color.white);
                        int diff = Integer.parseInt(newAmountField.getText()) - Integer.parseInt(amountField.getText());
                        if (diff > 0) {
                            delta.setText("+" + diff);
                            nameField.setBackground(Color.GREEN);
                        } else if (diff < 0) {
                            delta.setText(String.valueOf(diff));
                            nameField.setBackground(Color.RED);
                        } else {
                            delta.setText("0");
                            nameField.setBackground(Color.YELLOW);
                        }
                    } else {
                        delta.setText("");
                        nameField.setBackground(inventoryFrame.getBackground());
                    }
                } catch (NumberFormatException ex){
                    newAmountField.setBackground(Color.RED);
                }
            }
        });
        nameField.setEditable(false);
        amountField.setEditable(false);
        delta.setEditable(false);
        rowBox.add(nameField);
        rowBox.add(amountField);
        rowBox.add(Box.createRigidArea(new Dimension(5,0)));
        rowBox.add(newAmountField);
        rowBox.add(Box.createRigidArea(new Dimension(3,0)));
        rowBox.add(delta);
        rowBox.add(Box.createRigidArea(new Dimension(3,0)));
        rowBox.setName(drink.getName());
        mainBox.add(rowBox);
        boxArray.add(rowBox);
    }

    private void parseValues(){
        if(checkValues()) {
            JTextField delta;
            for (Box box : boxArray) {
                delta = (JTextField) box.getComponent(5);
                int amount = Integer.parseInt(delta.getText());
                if (amount != 0) {
                    gui.getController().sellAmount(box.getName(), -amount);
                }
            }
            close();
        }
    }

    private boolean checkValues() {
        JTextField delta;
        boolean correct = true;
        for(Box box:boxArray){
            delta = (JTextField) box.getComponent(5);
            delta.setBackground(inventoryFrame.getBackground());
            if(delta.getText().equals("")){
                delta.setBackground(Color.red);
                correct = false;
                break;
            }
        }
        return correct;
    }

    private void close(){
        inventoryFrame.dispose();
    }
}
