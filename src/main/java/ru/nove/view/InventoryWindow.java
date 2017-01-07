package ru.nove.view;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class InventoryWindow {
    private GUI gui;
    private String password;
    private JFrame inventoryFrame;
    private JScrollPane scrollPane;
    private Box listBox;
    private List boxList;
    JButton startButton, finishButton;

    public InventoryWindow(GUI gui) {
        this.gui = gui;
        password = "admin";

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

        listBox = new Box(BoxLayout.Y_AXIS);
        scrollPane = new JScrollPane(listBox, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        inventoryFrame.getContentPane().add(controlPanel, BorderLayout.NORTH);
        inventoryFrame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        inventoryFrame.pack();
        inventoryFrame.setLocationRelativeTo(gui.getMainFrame());
        inventoryFrame.setVisible(true);
    }

    private void startInventory() {
        startButton.setEnabled(true);
        finishButton.setEnabled(true);
    }

    private boolean checkPassword(char[] password) {
        return password.equals(this.password);
    }

    private void fillList(){

    }
}
