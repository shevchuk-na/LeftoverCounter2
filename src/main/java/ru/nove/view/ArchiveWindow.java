package ru.nove.view;

import ru.nove.model.entities.Drink;

import javax.swing.*;
import java.awt.*;

class ArchiveWindow {
    private GUI gui;
    private JFrame archiveFrame;
    private JTextField editName, editAmount;
    private Box listBox;
    private JButton editButton, okEditButton, makeObsoleteButton, cancelEditButton;
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

        JPanel editPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        editButton = new JButton("Изменить");
        gbc.gridx = gbc.gridy = 0;
        gbc.weightx = gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2,2,2,2);
        editPanel.add(editButton, gbc);
        editButton.setEnabled(false);
        makeObsoleteButton = new JButton("Убрать");
        gbc.gridx = 1;
        editPanel.add(makeObsoleteButton, gbc);
        makeObsoleteButton.setEnabled(false);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(15,2,5,2);
        editPanel.add(new JLabel("Название"), gbc);
        editName = new JTextField();
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(2,2,2,2);
        gbc.ipady = 5;
        editPanel.add(editName, gbc);
        editName.setEditable(false);
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.ipady = 0;
        editPanel.add(new JLabel("Размер порции"), gbc);
        editAmount = new JTextField();
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.ipady = 5;
        editPanel.add(editAmount, gbc);
        editAmount.setEditable(false);
        okEditButton = new JButton("Сохранить");
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.ipady = 0;
        gbc.weightx = gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.LAST_LINE_START;
        editPanel.add(okEditButton, gbc);
        okEditButton.setEnabled(false);
        cancelEditButton = new JButton("Отменить");
        gbc.gridx = 1;
        editPanel.add(cancelEditButton, gbc);
        cancelEditButton.setEnabled(false);
        scrollPane.setPreferredSize(new Dimension(200, 400));
        editPanel.setPreferredSize(new Dimension(200, 200));

        editButton.addActionListener(e -> {
            editMode = true;
            makeObsoleteButton.setEnabled(false);
            editName.setEditable(true);
            editAmount.setEditable(true);
            okEditButton.setEnabled(true);
            cancelEditButton.setEnabled(true);
        });
        makeObsoleteButton.addActionListener(e -> {
            obsoleteMode = true;
            editButton.setEnabled(false);
            editName.setText("БУДЕТ СКРЫТО:" + editName.getText());
            editAmount.setText("БУДЕТ СКРЫТО:" + editAmount.getText());
            okEditButton.setEnabled(true);
            cancelEditButton.setEnabled(true);
        });
        okEditButton.addActionListener(e -> {
            if(editMode){
                try {
                    gui.getController().editArchiveItem(drinkEdited, editName.getText(), Integer.parseInt(editAmount.getText()));
                    editMode = false;
                } catch (NumberFormatException ex){
                    editAmount.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.RED), BorderFactory.createEmptyBorder(3,2,3,2)));
                }
            } else if(obsoleteMode){
                gui.getController().makeItemObsolete(drinkEdited);
                obsoleteMode = false;
            }
            resetEditField();
            createDrinkList(listBox);
            gui.updatePosition(drinkEdited);        //TODO: updates deleted item: ArrayIndexOutOfBounds
        });
        cancelEditButton.addActionListener(e -> {
            if(editMode){
                editMode = false;
            } else if(obsoleteMode){
                obsoleteMode = false;
            }
            resetEditField();
        });

        archiveFrame.getContentPane().add(scrollPane, BorderLayout.WEST);
        archiveFrame.getContentPane().add(editPanel, BorderLayout.EAST);
        archiveFrame.pack();
        archiveFrame.setLocationRelativeTo(gui.getMainFrame());
        archiveFrame.setVisible(true);
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
                    editButton.setEnabled(true);
                    makeObsoleteButton.setEnabled(true);
                    editName.setText(drinkEdited.getName());
                    editAmount.setText(String.valueOf(drinkEdited.getDefaultAmount()));
                }
            });
            drinkGroup.add(item);
            listBox.add(item);
        }
        updateFrame();
    }

    private void resetEditField(){
        drinkGroup.clearSelection();
        editButton.setEnabled(false);
        makeObsoleteButton.setEnabled(false);
        okEditButton.setEnabled(false);
        cancelEditButton.setEnabled(false);
        editAmount.setText("");
        editName.setText("");
        editName.setEditable(false);
        editAmount.setEditable(false);
    }

    private void updateFrame(){
        archiveFrame.revalidate();
        archiveFrame.repaint();
    }

    void close(){
        archiveFrame.dispose();
    }
}
