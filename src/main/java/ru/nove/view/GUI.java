package ru.nove.view;

import ru.nove.controller.GraphicController;
import ru.nove.model.entities.Drink;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class GUI {
    private static final int AMOUNT = 1;
    private static final int SALE_BUTTON = 3;
    @SuppressWarnings("unused")
    private static final int CUSTOM_SALE_BUTTON = 5;
    @SuppressWarnings("unused")
    private static final int ADD_AMOUNT = 7;

    private GraphicController controller;
    private InputWindow inputWindow;
    private SortViewWindow sortViewWindow;
    private HistoryWindow historyWindow;
    private SellAddWindow sellAddWindow;
    private ArchiveWindow archiveWindow;
    private JTabbedPane tabbedPane;

    private JFrame mainFrame;
    private List<Box> boxArrayPlus, boxArrayMinus;
    private Box mainBoxPlus, mainBoxMinus;
    private JScrollPane activePane;
    private JTextArea logArea;
    private JButton cancelButton;
    private JTextField searchField;

    public GUI(GraphicController controller) {
        this.controller = controller;
        boxArrayPlus = new ArrayList<>();
        boxArrayMinus = new ArrayList<>();
    }

    public void createGui(){
        mainFrame = new JFrame("Счетчик плюсоминусов");
        tabbedPane = new JTabbedPane();
        JPanel mainPanelPlus = new JPanel();
        JPanel mainPanelMinus = new JPanel();
        mainBoxPlus = new Box(BoxLayout.Y_AXIS);
        mainBoxMinus = new Box(BoxLayout.Y_AXIS);
        mainBoxPlus.setAlignmentY(1);
        mainBoxMinus.setAlignmentY(1);
        mainPanelPlus.add(mainBoxPlus, BorderLayout.CENTER);
        mainPanelMinus.add(mainBoxMinus, BorderLayout.CENTER);
        JScrollPane scrollPanePlus = new JScrollPane(mainPanelPlus, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        Dimension scrollPaneDimension = new Dimension(600, 500);
        scrollPanePlus.setPreferredSize(scrollPaneDimension);
        JScrollPane scrollPaneMinus = new JScrollPane(mainPanelMinus, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPaneMinus.setPreferredSize(scrollPaneDimension);

        InputMap im = scrollPanePlus.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        for(int i = 0; i < 2; i++){
            im.put(KeyStroke.getKeyStroke("PAGE_UP"), "scrollUp");
            im.put(KeyStroke.getKeyStroke("PAGE_DOWN"), "scrollDown");
            im.put(KeyStroke.getKeyStroke("DOWN"), "unitScrollDown");
            im.put(KeyStroke.getKeyStroke("UP"), "unitScrollUp");
            im = scrollPaneMinus.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        }
        scrollPanePlus.getVerticalScrollBar().setUnitIncrement(15);
        scrollPaneMinus.getVerticalScrollBar().setUnitIncrement(15);

        activePane = scrollPanePlus;
        tabbedPane.add(scrollPanePlus);
        tabbedPane.add(scrollPaneMinus);
        JLabel plusLabel = new JLabel("Плюсы", JLabel.CENTER);
        Dimension tabDimension = new Dimension(scrollPaneDimension.width/2 -8, 25);
        plusLabel.setPreferredSize(tabDimension );
        tabbedPane.setTabComponentAt(0, plusLabel);
        JLabel minusLabel = new JLabel("Минусы", JLabel.CENTER);
        minusLabel.setPreferredSize(tabDimension);
        tabbedPane.setTabComponentAt(1, minusLabel);
        tabbedPane.addChangeListener(e -> activePane = (JScrollPane) tabbedPane.getSelectedComponent());

        JPanel topPanel = new JPanel();
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

        topPanel.add(addButton);
        topPanel.add(filterButton);
        topPanel.add(historyButton);
        topPanel.add(archiveButton);
        topPanel.add(cancelButton);
        topPanel.add(exitButton);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        logArea = new JTextArea();
        logArea.setPreferredSize(new Dimension(410, 40));
        logArea.setMargin(new Insets(2,5,2,5));
        logArea.setEditable(false);

        searchField = new JTextField(15);
        searchField.setFont(searchField.getFont().deriveFont(18f));
        searchField.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                highlightComponent();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                highlightComponent();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                highlightComponent();
            }

            List<Box> getActiveBoxArray() {
                if(activePane == scrollPanePlus){
                    return boxArrayPlus;
                } else {
                    return boxArrayMinus;
                }
            }
            void highlightComponent(){
                if(!searchField.getText().equals("")) {
                    for (Box box : getActiveBoxArray()) {
                        box.getComponent(0).setBackground(mainFrame.getBackground());
                        if (box.getName().toLowerCase().contains(searchField.getText())) {
                            box.getComponent(0).setBackground(Color.yellow);
                        }
                    }
                } else {
                    for (Box box : getActiveBoxArray()) {
                        box.getComponent(0).setBackground(mainFrame.getBackground());
                    }
                }
            }
        });

        bottomPanel.add(logArea, BorderLayout.WEST);
        bottomPanel.add(searchField, BorderLayout.EAST);

        Image image = new ImageIcon("res/icon.png").getImage();
        mainFrame.setIconImage(image);

        mainFrame.getContentPane().add(topPanel, BorderLayout.NORTH);
        mainFrame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
        mainFrame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        mainFrame.pack();
        mainFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        mainFrame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentHidden(ComponentEvent e) {
                controller.exit();
            }
        });
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setMinimumSize(new Dimension(650, 650));
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

    void sortPositions(SortViewModes order) {
        mainBoxPlus.removeAll();
        mainBoxMinus.removeAll();
        switch(order){
            case alphabetical:
                boxArrayPlus.sort(BoxComparators.getSortBoxByName());
                boxArrayMinus.sort(BoxComparators.getSortBoxByName());
                break;
            case available:
                boxArrayPlus.sort(BoxComparators.getSortByAmount());
                boxArrayMinus.sort(BoxComparators.getSortByAmount());
                break;
            case defAmount:
                boxArrayPlus.sort(BoxComparators.getSortByDefaultAmount());
                boxArrayMinus.sort(BoxComparators.getSortByDefaultAmount());
                break;
        }
        for(Box row:boxArrayPlus){
            mainBoxPlus.add(row);
        }
        for(Box row:boxArrayMinus){
            mainBoxMinus.add(row);
        }
        updateFrame();
    }

    private void exit() {
        controller.exit();
    }

    public void addPosition(Drink drink, boolean sort){
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
        saleButton.addActionListener(e -> sellDefaultAmount(nameField.getText()));
        JButton customSaleButton = new JButton("Продать");
        customSaleButton.setPreferredSize(new Dimension(85, 30));
        customSaleButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2,0,2,0), customSaleButton.getBorder()));
        customSaleButton.addActionListener(e -> createSellAddWindow(nameField.getText(), SellAddModes.customSale));
        JButton addAmountButton = new JButton("Добавить");
        addAmountButton.setPreferredSize(new Dimension(90, 30));
        addAmountButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2,0,2,0), addAmountButton.getBorder()));
        addAmountButton.addActionListener(e -> createSellAddWindow(nameField.getText(), SellAddModes.customAdd));
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
        if(drink.getAmount() > 0){
            boxArrayPlus.add(rowBox);
            mainBoxPlus.add(rowBox);
        } else {
            boxArrayMinus.add(rowBox);
            mainBoxMinus.add(rowBox);
        }
        if(sort) {
            sortPositions(SortViewModes.alphabetical);
        }
        showAddInfo(drink, drink.getAmount());
    }

    private void updateFrame() {
        mainFrame.revalidate();
        mainFrame.repaint();
        searchField.requestFocus();
    }

    private void sellDefaultAmount(String drink) {
        controller.sellAmount(drink);
    }

    public void showDrinks(List<Drink> drinks) {
        mainBoxPlus.removeAll();
        mainBoxMinus.removeAll();
        boxArrayPlus = new ArrayList<>();
        boxArrayMinus = new ArrayList<>();
        drinks.sort(Drink.getCompByName());
        for(Drink drink:drinks){
            addPosition(drink, false);
        }
        updateFrame();
    }

    public void updatePosition(Drink drink) {
        JTextField amount;
        JButton saleButton;
        if(drink.getAmount() < 0) {
            if(inPlusList(drink)){
                switchPosition(drink);
            }
        } else if(drink.getAmount() > 0){
            if(inMinusList(drink)){
                switchPosition(drink);
            }
        }
        if(drink.getAmount() > 0) {
            int index = getIndex(drink.getName(), boxArrayPlus);
            amount = (JTextField) boxArrayPlus.get(index).getComponent(AMOUNT);
            saleButton = (JButton) boxArrayPlus.get(index).getComponent(SALE_BUTTON);
        } else {
            int index = getIndex(drink.getName(), boxArrayMinus);
            amount = (JTextField) boxArrayMinus.get(index).getComponent(AMOUNT);
            saleButton = (JButton) boxArrayMinus.get(index).getComponent(SALE_BUTTON);
        }
        amount.setText(String.valueOf(drink.getAmount()));
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

    public void switchPosition(Drink drink){
        if(drink.getAmount() > 0){
            int index = getIndex(drink.getName(), boxArrayMinus);
            boxArrayMinus.remove(index);
            mainBoxMinus.remove(index);
        } else if(drink.getAmount() < 0){
            int index = getIndex(drink.getName(), boxArrayPlus);
            boxArrayPlus.remove(index);
            mainBoxPlus.remove(index);
        }
        addPosition(drink, true);
    }

    public void removePosition(Drink drink) {
        if(inPlusList(drink)){
            int index = getIndex(drink.getName(), boxArrayPlus);
            boxArrayPlus.remove(index);
            mainBoxPlus.remove(index);
        } else if(inMinusList(drink)){
            int index = getIndex(drink.getName(), boxArrayMinus);
            boxArrayMinus.remove(index);
            mainBoxMinus.remove(index);
        } else {
            showDeleteErrorWarning();
        }
        updateFrame();
    }

    private boolean inPlusList(Drink drink){
        for(Box box:boxArrayPlus){
            if(box.getName().equals(drink.getName())){
                return true;
            }
        }
        return false;
    }

    private boolean inMinusList(Drink drink){
        for(Box box:boxArrayMinus){
            if(box.getName().equals(drink.getName())){
                return true;
            }
        }
        return false;
    }

    public void displayHistory(List<String> history) {
        historyWindow.displayHistory(history);
    }

    private int getIndex(String name, List<Box> array){
        for(Box box:array){
            if(box.getName().equals(name)){
                return array.indexOf(box);
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

    private void showDeleteErrorWarning() {
        logArea.setText("Ошибка при удалении позиции");
    }
}