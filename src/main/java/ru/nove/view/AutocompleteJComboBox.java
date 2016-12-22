package ru.nove.view;

import ru.nove.model.entities.Drink;
import ru.nove.model.searchable.Searchable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.*;
import java.util.List;

class AutocompleteJComboBox extends JComboBox{

    private final Searchable<Drink, String> searchable;

    AutocompleteJComboBox(Searchable<Drink, String> s){
        super();
        setPreferredSize(new Dimension(200,25));
        this.searchable = s;
        setEditable(true);
        Component c = getEditor().getEditorComponent();
        if(c instanceof JTextComponent){
            final JTextComponent tc = (JTextComponent) c;
            tc.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    update();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                }

                void update(){
                    SwingUtilities.invokeLater(() -> {
                        List<Drink> founds = new ArrayList<>(searchable.search(tc.getText().toLowerCase()));
                        Set<String> foundSet = new HashSet<>();
                        for(Drink s1 : founds){
                            foundSet.add(s1.getName().toLowerCase());
                        }
                        Collections.sort(founds);

                        setEditable(false);
                        removeAllItems();

                        if(!foundSet.contains(tc.getText().toLowerCase())){
                            //noinspection unchecked
                            addItem(tc.getText());
                        }

                        for(Drink s1 :founds){
                            //noinspection unchecked
                            addItem(s1.getName());
                        }

                        setEditable(true);
                        setPopupVisible(true);
                        tc.requestFocus();
                    });
                }
            });

            tc.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    if(tc.getText().length() > 0){
                        setPopupVisible(true);
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                }
            });
        } else {
            throw new IllegalStateException("Editing component is not a JTextComponent");
        }

    }
}
