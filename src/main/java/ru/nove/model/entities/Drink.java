package ru.nove.model.entities;


import ru.nove.model.utils.DateUtil;

import java.io.Serializable;
import java.util.ArrayList;

public class Drink implements Serializable, Comparable<Drink>{
    private String name;
    private int amount;
    private int defaultAmount;
    private ArrayList<Integer> amountHistory;
    private ArrayList<Long> tsHistory;
    private final long serialVersionUID = -3573495;

    public Drink(String name, int amount) {
        this(name, amount, 50);
    }

    public Drink(String name, int amount, int defaultAmount){
        this.name = name;
        this.amount = amount;
        this.defaultAmount = defaultAmount;
        amountHistory = new ArrayList<>();
        amountHistory.add(amount);
        tsHistory = new ArrayList<>();
        tsHistory.add(DateUtil.now());
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
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

    public void addAmount(int amount){
        this.amount += amount;
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
}
