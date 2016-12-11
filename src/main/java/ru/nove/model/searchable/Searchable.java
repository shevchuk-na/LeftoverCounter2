package ru.nove.model.searchable;

import java.util.Collection;

public interface Searchable<E, V> {

    Collection<E> search(V value);
}
