package dnd.cli;

import dnd.cli.action_reader.ActionReader;
import dnd.cli.action_reader.ConsoleActionReader;
import dnd.cli.action_reader.FileActionReader;
import dnd.cli.printer.ConsolePrinter;
import dnd.cli.printer.Printer;
import dnd.cli.view.CliView;
import dnd.controllers.LevelController;
import dnd.logic.GameException;
import dnd.logic.random_generator.FileRandomGenerator;
import dnd.logic.random_generator.RandomGenerator;
import dnd.logic.random_generator.Randomizer;

import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        ActionReader actionReader;
        RandomGenerator randomGenerator;
        Printer printer = new ConsolePrinter();
        if (args == null || args.length == 0) {
            System.exit(1);
            return;
        }

        String levelFilesDirPath = getFilesDirPath(args[0]);
        if (args.length >= 2 && args[1].equals("-D")) {
            try {
                actionReader = new FileActionReader(getPath("user_actions.txt"), printer);
                randomGenerator = new FileRandomGenerator(getPath("random_numbers.txt"));
            }
            catch (FileNotFoundException e) {
                System.out.println("file not found.");
                e.printStackTrace();
                System.exit(2);
                return;
            }
        }
        else {
            actionReader = new ConsoleActionReader(new Scanner(System.in));
            randomGenerator = new Randomizer();
        }

        LevelController levelController = new LevelController(levelFilesDirPath, randomGenerator);
        CliView view = new CliView(printer, actionReader, levelController);
        try {
            view.startGame();
        }
        catch (GameException e) {
            System.out.println("game error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String getPath(String file) {
        return Paths.get(file).toString();
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
