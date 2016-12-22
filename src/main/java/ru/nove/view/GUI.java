package ru.nove.view;

import ru.nove.controller.GraphicController;
import ru.nove.model.entities.Drink;

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
    private static final String ALPHABETICAL = "alphabetical";
    private static final String AVAILABLE = "available";
    private GraphicController controller;
    private InputWindow inputWindow;
    private SortViewWindow sortViewWindow;
    private HistoryWindow historyWindow;
    private SellAddWindow sellAddWindow;
    private ArchiveWindow archiveWindow;
    
    private JFrame mainFrame;
    private List<Box> boxArray;
    private Box mainBox;
    private JTextArea logArea;
    private JButton cancelButton;

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
        JButton addButton = new JButton("Добавить позицию");
        addButton.addActionListener(e -> createInputWindow());
        JButton filterButton = new JButton("Вид");
        filterButton.addActionListener(e -> createViewWindow());
        JButton historyButton = new JButton("История операций");
        historyButton.addActionListener(e -> createHistoryWindow());
        JButton archiveButton = new JButton("Реестр");
        archiveButton.addActionListener(e -> createArchiveWindow());
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
        topButtonBox.add(archiveButton);
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

    GraphicController getController(){
        return controller;
    }

    private void createInputWindow() {
        if(inputWindow == null){
            inputWindow = new InputWindow(this);
        } else{
            inputWindow.showInputWindow();
        }
    }

    private void createViewWindow() {
        if(sortViewWindow == null) {
            sortViewWindow = new SortViewWindow(this);
        } else {
            sortViewWindow.showSortViewWindow();
        }
    }

    private void createHistoryWindow(){
        if(historyWindow == null) {
            historyWindow = new HistoryWindow(this);
            controller.viewHistory(HistoryModes.chronological);
        } else {
            historyWindow.showHistoryWindow();
        }
    }

    private void createSellAddWindow(String drink, SellAddModes command){
        if(sellAddWindow == null){
            sellAddWindow = new SellAddWindow(this, drink, command);
        } else {
            sellAddWindow.showSellAddWindow(drink, command);
        }
    }

    private void createArchiveWindow(){
        if(archiveWindow == null){
            archiveWindow = new ArchiveWindow(this);
        }else{
            archiveWindow.showArchiveWindow();
        }
    }

    void modifyView(SortViewModes order) {
        mainBox.removeAll();
        switch(order){
            case alphabetical:
                boxArray.sort(getSortBoxByName());
                break;
            case available:
                boxArray.sort(getSortByAmount());
                break;
            case defAmount:
                boxArray.sort(getSortByDefaultAmount());
                break;
        }
        for(Box row:boxArray){
            mainBox.add(row);
        }
        updateFrame();
    }

    private Comparator<Box> getSortByAmount() {
        return (d1, d2) -> {
            JTextField field1 = (JTextField) d1.getComponent(AMOUNT);
            JTextField field2 = (JTextField) d2.getComponent(AMOUNT);
            int amount1 = Integer.parseInt(field1.getText());
            int amount2 = Integer.parseInt(field2.getText());
            return amount2 - amount1;
        };
    }

    private Comparator<Box> getSortBoxByName() {
        return (d1, d2) -> d1.getName().compareTo(d2.getName());
    }

    private Comparator<Box> getSortByDefaultAmount() {
        return (d1, d2) -> {
            JButton one = (JButton) d1.getComponent(SALE_BUTTON);
            JButton two = (JButton) d2.getComponent(SALE_BUTTON);
            String[] defAmount1 = one.getText().split("Продать ");
            int defaultAmount1 = Integer.parseInt(defAmount1[1]);
            String[] defAmount2 = two.getText().split("Продать ");
            int defaultAmount2 = Integer.parseInt(defAmount2[1]);
            return defaultAmount1 - defaultAmount2;
        };
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
                    createSellAddWindow(sourcebox.getName(), SellAddModes.customSale);
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
                    createSellAddWindow(sourcebox.getName(), SellAddModes.customAdd);
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
        modifyView(SortViewModes.alphabetical);
    }

    private void updateFrame() {
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private void sellDefaultAmount(String drink) {
        controller.sellAmount(drink);
    }

    public void showDrinks(List<Drink> drinks) {
        mainBox.removeAll();
        boxArray = new ArrayList<>();
        drinks.sort(getSortDrinkByName());
        drinks.forEach(this::addPosition);
        updateFrame();
    }

    private Comparator<Drink> getSortDrinkByName() {
        return (d1, d2) -> d1.getName().compareTo(d2.getName());
    }

    public void updatePosition(Drink drink) {
        int index = getIndex(drink.getName());
        JTextField amount = (JTextField) boxArray.get(index).getComponent(AMOUNT);
        amount.setText(String.valueOf(drink.getAmount()));
        JButton saleButton = (JButton) boxArray.get(index).getComponent(SALE_BUTTON);
        saleButton.setText("Продать " + drink.getDefaultAmount());
        updateFrame();
    }

    public void closeAllFrames() {
        if(historyWindow != null) {
            historyWindow.close();
        }
        if(inputWindow != null) {
            inputWindow.close();
        }
        if(sellAddWindow != null){
            sellAddWindow.close();
        }
        if(sortViewWindow != null){
            sortViewWindow.close();
        }
        if(archiveWindow != null){
            archiveWindow.close();
        }
        mainFrame.dispose();

    }

    public void removePosition(String name) {
        int index = getIndex(name);
        boxArray.remove(index);
        mainBox.remove(index);
        updateFrame();
    }

    public void displayHistory(List<String> history) {
        historyWindow.displayHistory(history);
    }

    private int getIndex(String name){
        for(Box box:boxArray){
            if(box.getName().equals(name)){
                return boxArray.indexOf(box);
            }
        }
        return -1;
    }

    JFrame getMainFrame() {
        return mainFrame;
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

    void showNumberFormatWarning() {
        logArea.setText("Позиция НЕ добавлена\nВводите корректный данные в поля количества");
    }

    public void showSaleInfo(Drink drink, int amount) {
        logArea.setText("Продал " + amount + "мл " + drink.getName() + ". Осталось " + drink.getAmount());
    }

    public void showCancelInfo() {
        logArea.setText("Отмена");
    }

    public void showAddInfo(Drink drink, int amount) {
        logArea.setText("Добавил " + amount + "мл " + drink.getName() + ". Осталось " + drink.getAmount());
    }

    public void showNotEmptyWarning() {
        logArea.setText("Нельзя удалить позицию с ненулевым остатком!");
    }
}
