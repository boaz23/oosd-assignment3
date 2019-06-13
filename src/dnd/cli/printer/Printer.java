package dnd.cli.printer;

/**
 * Prints messages to anything depending on the implementation
 * (may be a file, terminal or even a composite printer).
 */
public interface Printer {
    void print(String line);
    void printLine(String line);
}
