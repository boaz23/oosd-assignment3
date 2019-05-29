package dnd.cli.action_reader;

import java.util.Scanner;

public class ConsoleActionReader implements ActionReader {
    private Scanner scanner;

    public ConsoleActionReader(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public String nextAction() {
        return scanner.nextLine();
    }
}
