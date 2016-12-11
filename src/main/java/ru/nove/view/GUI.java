package ru.nove.view;

import ru.nove.controller.GraphicController;
import ru.nove.model.entities.Drink;
import ru.nove.model.searchable.DrinkSearchable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GUI {
    private static final int AMOUNT = 1;
    private static final int SALE_BUTTON = 3;
    private static final int CUSTOM_SALE_BUTTON = 5;
    private static final int ADD_AMOUNT = 7;
    private static final String CUSTOM_SALE = "customSale";
    private static final String CUSTOM_ADD = "customAdd";
    private static final String ALPHABETICAL = "alphabetical";
    private static final String CHRONOLOGICAL = "chronological";
    private GraphicController controller;
    private JFrame mainFrame, historyFrame, inputFrame, customFrame, filterFrame;
    private JTextField amount, defaultAmount, nameField, saleAmount;
    private AutocompleteJComboBox name;
    private List<Box> boxArray;
    private Box mainBox;
    private JTextArea historyLog, logArea;
    private JRadioButton drinkOrderRadioButton;
    private JButton cancelButton, addButton, saleButton;

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
        scrollPane.setPreferredSize(new Dimension(585, 500));

        Box topButtonBox = new Box(BoxLayout.X_AXIS);
        JButton addButton = new JButton("Добавить позицию");
        addButton.addActionListener(e -> add());
        JButton filterButton = new JButton("Вид");
        filterButton.addActionListener(e -> filterView());
        JButton historyButton = new JButton("Просмотр истории");
        historyButton.addActionListener(e -> createHistoryWindow());
        cancelButton = new JButton("Отменить");
        cancelButton.addActionListener(e -> cancelLast());
        JButton exitButton = new JButton("Выход");
        exitButton.addActionListener(e -> exit());
        topButtonBox.add(addButton);
        topButtonBox.add(Box.createRigidArea(new Dimension(10,0)));
        topButtonBox.add(filterButton);
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

        Image image = new ImageIcon("res/icon.png").getImage();
        mainFrame.setIconImage(image);

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

    private void filterView() {
        if(filterFrame == null) {
            filterFrame = new JFrame("Вид");
            JPanel filterPanel = new JPanel();
            ButtonGroup viewGroup = new ButtonGroup();
            JRadioButton alphabetView = new JRadioButton("По алфавиту");
            alphabetView.setSelected(true);
            JRadioButton chronoView = new JRadioButton("По дате добавления");
            JButton applyViewButton = new JButton("Применить");
            applyViewButton.addActionListener(e -> {
                if(alphabetView.isSelected()){
                    modifyView(ALPHABETICAL);
                }else if(chronoView.isSelected()){
//                    modifyView(CHRONOLOGICAL);
                }
            });
            viewGroup.add(alphabetView);
            viewGroup.add(chronoView);
            filterPanel.add(alphabetView);
            filterPanel.add(chronoView);
            filterPanel.add(applyViewButton);
            filterPanel.setPreferredSize(new Dimension(300, 100));

            filterFrame.getContentPane().add(filterPanel);
            filterFrame.pack();
            filterFrame.setLocationRelativeTo(mainFrame);
            filterFrame.setVisible(true);
        } else {
            filterFrame.setVisible(true);
        }
    }

    private void modifyView(String order) {
        mainBox.removeAll();
        switch(order){
            case ALPHABETICAL:
                boxArray.sort(getSortByName());
                break;
            case CHRONOLOGICAL:
//                boxArray.sort(getSortByDefault());
                break;
        }

        for(Box row:boxArray){
            mainBox.add(row);
        }
        filterFrame.setVisible(false);
        updateFrame();
    }

//    private Comparator<Box> getSortByDefault() {
//        return (d1, d2) -> d1.getComponent(SALE_BUTT
//    }

    private Comparator<Box> getSortByName() {
        return (d1, d2) -> d1.getName().compareTo(d2.getName());
    }

    private void exit() {
        controller.exit();
    }

    public void addPosition(Drink drink){
        Box rowBox = new Box(BoxLayout.X_AXIS);
        JTextField nameField = new JTextField(drink.getName(), 18);
        nameField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2,2,2,0),nameField.getBorder()));
        nameField.setFont(nameField.getFont().deriveFont(Font.BOLD));
        JTextField amountField = new JTextField(String.valueOf(drink.getAmount()),5);
        amountField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2,2,2,0),amountField.getBorder()));
        amountField.setHorizontalAlignment(JTextField.CENTER);
        amountField.setFont(amountField.getFont().deriveFont(Font.BOLD));
        JButton saleButton = new JButton("Продать " + drink.getDefaultAmount());
        saleButton.setPreferredSize(new Dimension(110, 30));
        saleButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2,0,2,0),saleButton.getBorder()));
        saleButton.addActionListener(e -> {
            for(Box sourceBox:boxArray){
                if(e.getSource() == sourceBox.getComponent(SALE_BUTTON)){
                    sellDefaultAmount(sourceBox.getName());
                    break;
                }
            }
        });
        JButton customSaleButton = new JButton("Продать");
        customSaleButton.setPreferredSize(new Dimension(85, 30));
        customSaleButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2,0,2,0), customSaleButton.getBorder()));
        customSaleButton.addActionListener(e -> {
            for(Box sourcebox:boxArray){
                if(e.getSource() == sourcebox.getComponent(CUSTOM_SALE_BUTTON)){
                    createCustomWindow(sourcebox.getName(), CUSTOM_SALE);
                    break;
                }
            }
        });
        JButton addAmountButton = new JButton("Добавить");
        addAmountButton.setPreferredSize(new Dimension(90, 30));
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
            customFrame = new JFrame();
            saleButton = new JButton();
            switch (command){
                case CUSTOM_SALE:
                    customFrame.setTitle("Продать количество");
                    saleButton.setText("Продать");
                    break;
                case CUSTOM_ADD:
                    customFrame.setTitle("Добавить количество");
                    saleButton.setText("Добавить");
                    break;
            }
            JPanel salePanel = new JPanel();
            nameField = new JTextField(drink, 10);
            nameField.setEditable(false);
            nameField.setMargin(new Insets(3,2,3,2));
            saleAmount = new JTextField(5);
            saleAmount.setMargin(new Insets(3,2,3,2));
            saleButton.addActionListener(e -> performCustomAction(nameField.getText(), saleAmount.getText(), saleButton.getText()));
            salePanel.add(nameField);
            salePanel.add(saleAmount);
            salePanel.add(saleButton);
            customFrame.getContentPane().add(salePanel);
            customFrame.setSize(new Dimension(310, 80));
            customFrame.setLocationRelativeTo(mainFrame);
            customFrame.setVisible(true);
            saleAmount.requestFocus();
        } else {
            switch (command){
                case CUSTOM_SALE:
                    customFrame.setTitle("Продать количество");
                    saleButton.setText("Продать");
                    break;
                case CUSTOM_ADD:
                    customFrame.setTitle("Добавить количество");
                    saleButton.setText("Добавить");
                    break;
            }
            nameField.setText(drink);
            saleAmount.setText("");
            saleAmount.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY), BorderFactory.createEmptyBorder(3,2,3,2)));
            customFrame.setVisible(true);
            saleAmount.requestFocus();
        }
    }

    private void performCustomAction(String name, String amount, String command){
        try {
            if(amount.equals("")){
                saleAmount.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.RED), BorderFactory.createEmptyBorder(3,2,3,2)));
            } else {
                customFrame.setVisible(false);
                switch(command){
                    case "Продать":
                        controller.sellAmount(name, Integer.parseInt(amount));
                        break;
                    case "Добавить":
                        controller.addAmount(name, Integer.parseInt(amount));
                        break;
                }
            }
        }catch (NumberFormatException ex){
            ex.printStackTrace();
            showNumberFormatWarning();
        }
    }

    private void updateFrame() {
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private void sellDefaultAmount(String drink) {
        controller.sellAmount(drink);
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
            name.requestFocus();
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
            name.requestFocus();
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

    private void addData() {
        inputFrame.setVisible(false);
        try {
            controller.addDrink(((String) name.getSelectedItem()), Integer.parseInt(amount.getText()), Integer.parseInt(defaultAmount.getText()));
        }catch (NumberFormatException e){
            showNumberFormatWarning();
        }
    }

    private void showNumberFormatWarning() {
        logArea.setText("Позиция НЕ добавлена\nВводите корректный данные в поля количества");
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
        if(filterFrame != null){
            filterFrame.dispose();
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
            historyFrame = new JFrame("История операций");
            historyLog = new JTextArea();
            JScrollPane scrollPane = new JScrollPane(historyLog, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setPreferredSize(new Dimension(400, 300));
            Box buttonBox = new Box(BoxLayout.X_AXIS);
            drinkOrderRadioButton = new JRadioButton("По наименованию");
            drinkOrderRadioButton.setSelected(true);
            JRadioButton chronoOrderRadioButton = new JRadioButton("По хронологии");
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
        logArea.setText("Данные загружены");
    }

    public void showLoadError() {
        logArea.setText("Ошибка загрузки данных");
    }

    public void showSaveError() {
        logArea.setText("Ошибка сохранения");
    }

    public void showLoadedDrinks(List<Drink> drinks) {
        drinks.forEach(this::addPosition);
    }

    public void enableCancelButton(boolean b) {
        cancelButton.setEnabled(b);
    }

    public void showNewSaveFileInfo() {
        logArea.setText("Создан новый файл сохранения");
    }

    public void showDuplicateWarning() {
        logArea.setText("Позиция уже в списке");
    }

    public void showAutoSaveInfo() {
        logArea.setText("Автосохранение данных...");
    }
}
