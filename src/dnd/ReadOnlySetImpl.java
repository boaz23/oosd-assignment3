package dnd;

import java.util.Iterator;
import java.util.Set;

public class ReadOnlySetImpl<E> implements ReadOnlySet<E> {
    private final Set<E> set;

    public ReadOnlySetImpl(Set<E> set) {
        if (set == null) {
            throw new IllegalArgumentException("set is null.");
        }

        this.set = set;
    }

    @Override
    public int size() {
        return this.set.size();
    }

    @Override
    public boolean isEmpty() {
        return this.set.isEmpty();
    }

    @Override
    public boolean contains(E e) {
        return this.set.contains(e);
    }

    @Override
    public boolean containsAll(Set<E> set) {
        return this.set.containsAll(set);
    }

    @Override
    public Iterator<E> iterator() {
        return this.set.iterator();
    }
}
