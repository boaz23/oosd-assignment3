package dnd.cli;

import dnd.cli.action_reader.ActionReader;
import dnd.cli.action_reader.ConsoleActionReader;
import dnd.cli.action_reader.FileActionReader;
import dnd.cli.printer.ConsolePrinter;
import dnd.cli.printer.Printer;
import dnd.cli.view.CliView;
import dnd.controllers.LevelController;
import dnd.logic.random_generator.FileRandomGenerator;
import dnd.logic.random_generator.RandomGenerator;
import dnd.logic.random_generator.Randomizer;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static int main(String[] args) {
        ActionReader actionReader;
        RandomGenerator randomGenerator;
        Printer printer = new ConsolePrinter();
        if (args == null || args.length == 0) {
            return 1;
        }

        String filesDirPath = getFilesDirPath(args[0]);
        if (args.length >= 2 && args[1].equals("-D")) {
            try {
                actionReader = new FileActionReader(filesDirPath + "user_actions.txt");
                randomGenerator = new FileRandomGenerator(filesDirPath + "random_numbers.txt");
            } catch (FileNotFoundException e) {
                System.out.println("file not found.");
                e.printStackTrace();
                return 2;
            }
        }
        else {
            actionReader = new ConsoleActionReader(new Scanner(System.in));
            randomGenerator = new Randomizer();
        }

        LevelController levelController = new LevelController(filesDirPath, randomGenerator);
        CliView view  = new CliView(printer, actionReader, levelController);
        view.startGame();
        return 0;
    }

    private static String getFilesDirPath(String arg) {
        String filesDirPath = arg;
        char lastPathChar = filesDirPath.charAt(filesDirPath.length() - 1);
        if (lastPathChar != '/' & lastPathChar != '\\') {
            filesDirPath += "\\";
        }
        return filesDirPath;
    }
}
