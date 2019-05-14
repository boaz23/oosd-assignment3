package interfaces;

public interface Observer<TEventArgs> {
    void callback(Observable<TEventArgs> observable, TEventArgs args);
}
