package dnd.logic;

import java.util.Set;

public interface ReadOnlySet<E> extends Iterable<E> {
    int size();
    boolean isEmpty();
    boolean contains(E e);
    boolean containsAll(Set<E> set);
}
