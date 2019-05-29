package dnd.cli.printer;

public class ConsolePrinter implements Printer {
    @Override
    public void print(String line) {
        System.out.println(line);
    }
}
