package ru.nove.view;

import ru.nove.controller.GraphicController;
import ru.nove.model.entities.Drink;
import ru.nove.model.searchable.DrinkSearchable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class GUI {
    private static final int AMOUNT = 1;
    private static final int SALE_BUTTON = 3;
    private static final int CUSTOM_SALE_BUTTON = 5;
    private static final int ADD_AMOUNT = 7;
    private static final String CUSTOM_SALE = "customSale";
    private static final String CUSTOM_ADD = "customAdd";
    private GraphicController controller;
    private JFrame mainFrame, historyFrame, inputFrame, customFrame;
    private JTextField amount, defaultAmount, nameField, saleAmount;
    private AutocompleteJComboBox name;
    private List<Box> boxArray;
    private Box mainBox;
    private JTextArea historyLog, logArea;
    private JRadioButton drinkOrderRadioButton;
    private JButton cancelButton, addButton;

    public GUI(GraphicController controller) {
        this.controller = controller;
        boxArray = new ArrayList<>();
    }

    public void createGui(){
        mainFrame = new JFrame("Счетчик плюсоминусов");
        JPanel mainPanel = new JPanel();
        mainBox = new Box(BoxLayout.Y_AXIS);
        mainBox.setAlignmentY(1);
        mainPanel.add(mainBox, BorderLayout.CENTER);
        JScrollPane scrollPane = new JScrollPane(mainPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(600, 500));

        Box topButtonBox = new Box(BoxLayout.X_AXIS);
        JButton addButton = new JButton("Add drink");
        addButton.addActionListener(e -> add());
        JButton historyButton = new JButton("View history");
        historyButton.addActionListener(e -> createHistoryWindow());
        cancelButton = new JButton("Cancel last");
        cancelButton.addActionListener(e -> cancelLast());
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> exit());
        topButtonBox.add(addButton);
        topButtonBox.add(Box.createRigidArea(new Dimension(10,0)));
        topButtonBox.add(historyButton);
        topButtonBox.add(Box.createRigidArea(new Dimension(10,0)));
        topButtonBox.add(cancelButton);
        topButtonBox.add(Box.createRigidArea(new Dimension(10,0)));
        topButtonBox.add(exitButton);
        topButtonBox.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));

        logArea = new JTextArea();
        logArea.setPreferredSize(new Dimension(500, 40));
        logArea.setMargin(new Insets(2,5,2,5));

        mainFrame.getContentPane().add(topButtonBox, BorderLayout.NORTH);
        mainFrame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        mainFrame.getContentPane().add(logArea, BorderLayout.SOUTH);
        mainFrame.pack();
        mainFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        mainFrame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentHidden(ComponentEvent e) {
                controller.exit();
            }
        });
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    private void exit() {
        controller.exit();
    }

    public void addPosition(Drink drink){
        Box rowBox = new Box(BoxLayout.X_AXIS);
        JTextField nameField = new JTextField(drink.getName(),30);
        nameField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2,0,2,0),nameField.getBorder()));
        JTextField amountField = new JTextField(String.valueOf(drink.getAmount()),5);
        amountField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2,0,2,0),amountField.getBorder()));
        JButton saleButton = new JButton("Sell " + drink.getDefaultAmount());
        saleButton.setPreferredSize(new Dimension(90, 30));
        saleButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2,0,2,0),saleButton.getBorder()));
        saleButton.addActionListener(e -> {
            for(Box sourceBox:boxArray){
                if(e.getSource() == sourceBox.getComponent(SALE_BUTTON)){
                    sellDefaultAmount(sourceBox.getName());
                    break;
                }
            }
        });
        JButton customSaleButton = new JButton("Sell ...");
        customSaleButton.setPreferredSize(new Dimension(70, 30));
        customSaleButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2,0,2,0), customSaleButton.getBorder()));
        customSaleButton.addActionListener(e -> {
            for(Box sourcebox:boxArray){
                if(e.getSource() == sourcebox.getComponent(CUSTOM_SALE_BUTTON)){
                    createCustomWindow(sourcebox.getName(), CUSTOM_SALE);
                    break;
                }
            }
        });
        JButton addAmountButton = new JButton("Add ...");
        addAmountButton.setPreferredSize(new Dimension(70, 30));
        addAmountButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2,0,2,0), addAmountButton.getBorder()));
        addAmountButton.addActionListener(e -> {
            for(Box sourcebox:boxArray){
                if(e.getSource() == sourcebox.getComponent(ADD_AMOUNT)){
                    createCustomWindow(sourcebox.getName(), CUSTOM_ADD);
                    break;
                }
            }
        });
        nameField.setEditable(false);
        amountField.setEditable(false);
        rowBox.add(nameField);
        rowBox.add(amountField);
        rowBox.add(Box.createRigidArea(new Dimension(5,0)));
        rowBox.add(saleButton);
        rowBox.add(Box.createRigidArea(new Dimension(3,0)));
        rowBox.add(customSaleButton);
        rowBox.add(Box.createRigidArea(new Dimension(3,0)));
        rowBox.add(addAmountButton);
        rowBox.setName(drink.getName());
        boxArray.add(rowBox);
        mainBox.add(rowBox);
        updateFrame();
    }

    private void createCustomWindow(String drink, String command) {
        if(customFrame == null){
            JButton saleButton = null;
            switch(command){
                case CUSTOM_SALE:
                    customFrame = new JFrame("Custom sale");
                    saleButton = new JButton("Sell");
                    break;
                case CUSTOM_ADD:
                    customFrame = new JFrame("Add amount");
                    saleButton = new JButton("Add");
                    break;
            }
            JPanel salePanel = new JPanel();
            nameField = new JTextField(drink, 10);
            nameField.setEditable(false);
            nameField.setMargin(new Insets(3,2,3,2));
            saleAmount = new JTextField(5);
            saleAmount.setMargin(new Insets(3,2,3,2));
            saleButton.addActionListener(e -> {
                try {
                    if(saleAmount.getText().equals("")){
                        saleAmount.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.RED), BorderFactory.createEmptyBorder(3,2,3,2)));
                    } else {
                        customFrame.setVisible(false);
                        switch(command){
                            case CUSTOM_SALE:
                                controller.sellAmount(nameField.getText(), Integer.parseInt(saleAmount.getText()));
                                break;
                            case CUSTOM_ADD:
                                controller.addAmount(nameField.getText(), Integer.parseInt(saleAmount.getText()));
                                break;
                        }
                    }
                }catch (NumberFormatException ex){
                    ex.printStackTrace();
                    showNumberFormatWarning();
                }
            });
            salePanel.add(nameField);
            salePanel.add(saleAmount);
            salePanel.add(saleButton);
            customFrame.getContentPane().add(salePanel);
            customFrame.setSize(new Dimension(280, 90));
            customFrame.setLocationRelativeTo(mainFrame);
            customFrame.setVisible(true);
        } else {
            nameField.setText(drink);
            saleAmount.setText("");
            saleAmount.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY), BorderFactory.createEmptyBorder(3,2,3,2)));
            customFrame.setVisible(true);
        }
    }

    private void updateFrame() {
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private void sellDefaultAmount(String drink) {
        controller.sellAmount(drink);
    }

    private void sellAmount(String drink, int amount){
        controller.sellAmount(drink, amount);
    }

    private void add() {
        createInputWindow();
    }

    private void createInputWindow(){
        if(inputFrame != null){
            inputFrame.setVisible(true);
            name.setSelectedItem(null);
            amount.setText("");
            defaultAmount.setText("");
            addButton.setEnabled(false);
        } else {
            inputFrame = new JFrame("Введите данные");
            JPanel panel = new JPanel();
            amount = new JTextField(6);
            amount.setMargin(new Insets(3,2,3,2));
            JLabel defAmoLabel = new JLabel("Продажа по умолчанию:");
            defaultAmount = new JTextField(5);
            defaultAmount.setMargin(new Insets(3,2,3,2));
            name = new AutocompleteJComboBox(new DrinkSearchable(controller.getRegistry()));
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
            addButton.addActionListener( (e) -> addData());
            panel.add(name);
            panel.add(amount);
            panel.add(defAmoLabel);
            panel.add(defaultAmount);
            panel.add(addButton);
            inputFrame.setSize(320, 145);
            inputFrame.getContentPane().add(panel);
            inputFrame.setLocationRelativeTo(mainFrame);
            inputFrame.setVisible(true);
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
            int defAmount = controller.checkRegistry(drink);
            if (defAmount != 0) {
                defaultAmount.setText(String.valueOf(defAmount));
            }
        }
    }

    private void clearText() {

    }

    private void addData() {
        inputFrame.setVisible(false);
        try {
            controller.addDrink(((String) name.getSelectedItem()), Integer.parseInt(amount.getText()), Integer.parseInt(defaultAmount.getText()));
        }catch (NumberFormatException e){
            showNumberFormatWarning();
        }
    }

    private void showNumberFormatWarning() {
        logArea.setText("DRINK NOT ADDED!\nOnly numbers permitted in amount fields");
    }

    public void updateAmount(String name, int amount) {
        int index = getIndex(name);
        JTextField field = (JTextField) boxArray.get(index).getComponent(AMOUNT);
        field.setText(String.valueOf(amount));
    }

    public void closeAllFrames() {
        if(historyFrame != null) {
            historyFrame.dispose();
        }
        if(inputFrame != null) {
            inputFrame.dispose();
        }
        if(customFrame != null){
            customFrame.dispose();
        }
        mainFrame.dispose();

    }

    public void removePosition(String name) {
        int index = getIndex(name);
        boxArray.remove(index);
        mainBox.remove(index);
        updateFrame();
    }

    private void createHistoryWindow(){
        if(historyFrame != null){
            historyFrame.setVisible(true);
            updateHistory();
        }else {
            historyFrame = new JFrame("History");
            historyLog = new JTextArea();
            JScrollPane scrollPane = new JScrollPane(historyLog, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setPreferredSize(new Dimension(400, 300));
            Box buttonBox = new Box(BoxLayout.X_AXIS);
            drinkOrderRadioButton = new JRadioButton("By drink");
            drinkOrderRadioButton.setSelected(true);
            JRadioButton chronoOrderRadioButton = new JRadioButton("By order");
            JButton closeButton = new JButton("Exit");
            closeButton.addActionListener(e -> historyFrame.setVisible(false));
            JButton updateButton = new JButton("Update");
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
            historyFrame.setLocationRelativeTo(mainFrame);
            historyFrame.setVisible(true);
            controller.viewHistory(1);
        }
    }

    private void updateHistory() {
        if(drinkOrderRadioButton.isSelected()){
            controller.viewHistory(1);
        }else{
            controller.viewHistory(2);
        }
    }

    public void displayHistory(List<String> history) {
        historyLog.setText("");
        for(String line:history){
            historyLog.append(line+"\n");
        }

    }

    private int getIndex(String name){
        for(Box box:boxArray){
            if(box.getName().equals(name)){
                return boxArray.indexOf(box);
            }
        }
        return -1;
    }

    private void cancelLast(){
        controller.cancelLast();
    }

    public void showLoadedInfo() {
        logArea.setText("Data loaded!");
    }

    public void showLoadError() {
        logArea.setText("Loading error!");
    }

    public void showSaveError() {
        logArea.setText("Save error!");
    }

    public void showLoadedDrinks(List<Drink> drinks) {
        drinks.forEach(this::addPosition);
    }

    public void enableCancelButton(boolean b) {
        cancelButton.setEnabled(b);
    }

    public void showNewSaveFileInfo() {
        logArea.setText("New save file created!");
    }

    public void showDuplicateWarning() {
        logArea.setText("Drink already in the list!");
    }

    public void showAutoSaveInfo() {
        logArea.setText("Autosave complete!");
    }
}
