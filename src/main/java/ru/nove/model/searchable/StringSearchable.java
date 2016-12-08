package ru.nove.model.searchable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StringSearchable implements Searchable<String, String> {

    private List<String> terms = new ArrayList<>();

    public StringSearchable(List<String> terms){
        this.terms.addAll(terms);
    }

    @Override
    public Collection<String> search(String value) {
        List<String> founds = new ArrayList<>();

        for(String s:terms){
            if(s.contains(value)){
                founds.add(s);
            }
        }
        return founds;
    }
}
