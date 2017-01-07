package ru.nove.view;

import ru.nove.model.entities.Drink;

import javax.swing.*;
import java.awt.*;

class ArchiveWindow {
    private GUI gui;
    private JFrame archiveFrame;
    private JTextField editNameField, editAmountField;
    private Box listBox;
    private JButton editNameButton, okButton, makeObsoleteButton, cancelButton;
    private ButtonGroup drinkGroup;
    private boolean editMode, obsoleteMode;
    private Drink drinkEdited;

    ArchiveWindow(GUI gui){
        this.gui = gui;
        archiveFrame = new JFrame("Реестр напитков");
        archiveFrame.setLayout(new BorderLayout());

        listBox = new Box(BoxLayout.Y_AXIS);
        JScrollPane scrollPane = new JScrollPane(listBox, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        drinkGroup = new ButtonGroup();
        createDrinkList(listBox);
        JButton inventoryButton = new JButton("Начать инвентуру!");
        inventoryButton.addActionListener(e -> openInventoryWindow());
        inventoryButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2,2,2,2), inventoryButton.getBorder()));
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.add(scrollPane, BorderLayout.CENTER);
        listPanel.add(inventoryButton, BorderLayout.SOUTH);

        JPanel editPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        editNameButton = new JButton("Изменить");
        gbc.gridx = gbc.gridy = 0;
        gbc.weightx = gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2,2,2,2);
        editPanel.add(editNameButton, gbc);
        editNameButton.setEnabled(false);
        makeObsoleteButton = new JButton("Убрать");
        gbc.gridx = 1;
        editPanel.add(makeObsoleteButton, gbc);
        makeObsoleteButton.setEnabled(false);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(15,2,5,2);
        editPanel.add(new JLabel("Название"), gbc);
        editNameField = new JTextField();
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(2,2,2,2);
        gbc.ipady = 5;
        editPanel.add(editNameField, gbc);
        editNameField.setEditable(false);
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.ipady = 0;
        editPanel.add(new JLabel("Размер порции"), gbc);
        editAmountField = new JTextField();
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.ipady = 5;
        editPanel.add(editAmountField, gbc);
        editAmountField.setEditable(false);
        okButton = new JButton("Сохранить");
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.ipady = 0;
        gbc.weightx = gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.LAST_LINE_START;
        editPanel.add(okButton, gbc);
        okButton.setEnabled(false);
        cancelButton = new JButton("Отменить");
        gbc.gridx = 1;
        editPanel.add(cancelButton, gbc);
        cancelButton.setEnabled(false);
        scrollPane.setPreferredSize(new Dimension(200, 400));
        editPanel.setPreferredSize(new Dimension(200, 200));

        editNameButton.addActionListener(e -> {
            editMode = true;
            makeObsoleteButton.setEnabled(false);
            editNameButton.setEnabled(false);
            editNameField.setEditable(true);
            editAmountField.setEditable(true);
            okButton.setEnabled(true);
            cancelButton.setEnabled(true);
        });
        makeObsoleteButton.addActionListener(e -> {
            obsoleteMode = true;
            editNameButton.setEnabled(false);
            makeObsoleteButton.setEnabled(false);
            editNameField.setText("УБРАТЬ:" + editNameField.getText());
            editAmountField.setText("УБРАТЬ:" + editAmountField.getText());
            okButton.setEnabled(true);
            cancelButton.setEnabled(true);
        });
        okButton.addActionListener(e -> {
            if(editMode){
                try {
                    gui.getController().editArchiveItem(drinkEdited, editNameField.getText(), Integer.parseInt(editAmountField.getText()));
                    gui.updatePosition(drinkEdited);
                    editMode = false;
                } catch (NumberFormatException ex){
                    editAmountField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.RED), BorderFactory.createEmptyBorder(3,2,3,2)));
                }
            } else if(obsoleteMode){
                gui.getController().makeItemObsolete(drinkEdited);
                obsoleteMode = false;
            }
            resetEditField();
            createDrinkList(listBox);
        });
        cancelButton.addActionListener(e -> {
            editMode = false;
            obsoleteMode = false;
            resetEditField();
        });

        archiveFrame.getContentPane().add(listPanel, BorderLayout.WEST);
        archiveFrame.getContentPane().add(editPanel, BorderLayout.EAST);
        archiveFrame.pack();
        archiveFrame.setLocationRelativeTo(gui.getMainFrame());
        archiveFrame.setVisible(true);
    }

    private void openInventoryWindow() {
        InventoryWindow inventory = new InventoryWindow(gui);
    }

    void showArchiveWindow() {
        if(archiveFrame != null) {
            createDrinkList(listBox);
            drinkGroup.clearSelection();
            resetEditField();
            archiveFrame.setVisible(true);
        }
    }

    private void createDrinkList(Box listBox){
        listBox.removeAll();
        for (Drink drink : gui.getController().getActualArchive()) {
            JRadioButton item = new JRadioButton(drink.getName());
            item.addActionListener(e -> {
                if(item.isSelected()){
                    drinkEdited = drink;
                    returnNormalMode();
                }
            });
            drinkGroup.add(item);
            listBox.add(item);
        }
        updateFrame();
    }

    private void returnNormalMode(){
        editNameButton.setEnabled(true);
        makeObsoleteButton.setEnabled(true);
        editMode = false;
        obsoleteMode = false;
        editNameField.setEditable(false);
        editAmountField.setEditable(false);
        okButton.setEnabled(false);
        cancelButton.setEnabled(false);
        editNameField.setText(drinkEdited.getName());
        editAmountField.setText(String.valueOf(drinkEdited.getDefaultAmount()));
    }

    private void resetEditField(){
        drinkGroup.clearSelection();
        editNameButton.setEnabled(false);
        makeObsoleteButton.setEnabled(false);
        okButton.setEnabled(false);
        cancelButton.setEnabled(false);
        editAmountField.setText("");
        editNameField.setText("");
        editNameField.setEditable(false);
        editAmountField.setEditable(false);
    }

    private void updateFrame(){
        archiveFrame.revalidate();
        archiveFrame.repaint();
    }

    void close(){
        archiveFrame.dispose();
    }
}
