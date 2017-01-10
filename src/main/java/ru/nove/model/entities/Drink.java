package ru.nove.model.entities;


import ru.nove.model.utils.DateUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

public class Drink implements Serializable, Comparable<Drink>{
    private String name;
    private int amount;
    private int defaultAmount;
    private ArrayList<Integer> amountHistory;
    private ArrayList<Long> tsHistory;
    private boolean obsolete;
    private static final long serialVersionUID = -3573495L;

    public Drink(String name, int amount, int defaultAmount){
        this.name = name;
        this.amount = amount;
        this.defaultAmount = defaultAmount;
        amountHistory = new ArrayList<>();
        amountHistory.add(amount);
        tsHistory = new ArrayList<>();
        tsHistory.add(DateUtil.now());
        obsolete = false;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDefaultAmount(int defaultAmount) {
        this.defaultAmount = defaultAmount;
    }

    public ArrayList<Integer> getAmountHistory() {
        return amountHistory;
    }

    public ArrayList<Long> getTsHistory() {
        return tsHistory;
    }

    public int getDefaultAmount() {
        return defaultAmount;
    }

    public boolean isObsolete() {
        return obsolete;
    }

    public void setObsolete(boolean obsolete) {
        this.obsolete = obsolete;
    }

    public void addAmount(int add){
        this.amount += add;
        amountHistory.add(amount);
        tsHistory.add(DateUtil.now());
    }

    public void addSale(int sale){
        amount -= sale;
        amountHistory.add(amount);
        tsHistory.add(DateUtil.now());
    }

    public void removeSale(){
        if(amountHistory.size() > 1) {
            int last = amountHistory.size() - 1;
            amount = amountHistory.get(last-1);
            amountHistory.remove(last);
            tsHistory.remove(last);
        }
    }

    @Override
    public int compareTo(Drink o) {
        return this.getName().compareTo(o.getName());
    }

    public static Comparator<Drink> getCompByName(){
        return Comparator.comparing(Drink::getName);
    }

    public static Comparator<Drink> getCompByAmount(){
        return Comparator.comparing(Drink::getAmount);
    }

    public static Comparator<Drink> getCompByNameEmptyLast(){
        return (d1, d2) -> {
            if(d1.getAmount() == 0) return 1;
            else if(d2.getAmount() == 0) return -1;
            else return d1.compareTo(d2);
        };
    }
}
