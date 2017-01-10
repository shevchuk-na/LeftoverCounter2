package ru.nove.view;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;

abstract class BoxComparators {
    private static final int AMOUNT = 1;
    private static final int SALE_BUTTON = 3;
    private static final int CUSTOM_SALE_BUTTON = 5;
    private static final int ADD_AMOUNT = 7;

    static Comparator<Box> getSortByAmount() {
        return (d1, d2) -> {
            JTextField field1 = (JTextField) d1.getComponent(AMOUNT);
            JTextField field2 = (JTextField) d2.getComponent(AMOUNT);
            int amount1 = Integer.parseInt(field1.getText());
            int amount2 = Integer.parseInt(field2.getText());
            return amount2 - amount1;
        };
    }

    static Comparator<Box> getSortBoxByName() {
        return Comparator.comparing(Component::getName);
    }

    static Comparator<Box> getSortByDefaultAmount() {
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
}
