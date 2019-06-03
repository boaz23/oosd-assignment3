package dnd.cli;

import dnd.cli.action_reader.ActionReader;
import dnd.cli.printer.Printer;
import dnd.controllers.ActionController;
import dnd.controllers.LevelController;

// TODO: finish methods and write the game loop somewhere
public class CliView extends CliEventsView {
    private final Printer printer;
    private final ActionReader actionReader;
    private final LevelController levelController;
    private ActionController actionController;
    private int currentLevel;

    public CliView(Printer printer, ActionReader actionReader, LevelController levelController) {
        if (printer == null) {
            throw new IllegalArgumentException("printer is null.");
        }
        if (actionReader == null) {
            throw new IllegalArgumentException("action reader is null.");
        }
        if (levelController == null) {
            throw new IllegalArgumentException("level controller is null.");
        }

        this.printer = printer;
        this.actionReader = actionReader;
        this.levelController = levelController;
        this.currentLevel = 0;
    }

    public void loadNextLevel() {
        this.currentLevel++;
        this.actionController = this.levelController.loadLevel(this.currentLevel);
    }

    public void act() {
        String nextAction = this.actionReader.nextAction();
    }
}
