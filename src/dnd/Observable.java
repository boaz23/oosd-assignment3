package dnd;

public interface Observable<TEventArgs> {
    void addObserver(Observer<TEventArgs> observer);
}
