package dnd.cli.view;

import dnd.cli.GameFinishedResult;
import dnd.cli.action_reader.ActionReader;
import dnd.cli.printer.Printer;
import dnd.controllers.ActionController;
import dnd.controllers.LevelController;

import java.util.HashMap;

// TODO: print character select menu, the selection and the actions on game start
public class CliView extends PrintEventsView {
    private static final HashMap<String, PlayerAction> actions = new HashMap<String, PlayerAction>() {{
       put("w", actionController -> actionController.moveUp());
       put("s", actionController -> actionController.moveDown());
       put("a", actionController -> actionController.moveLeft());
       put("d", actionController -> actionController.moveRight());
       put("e", actionController -> actionController.castSpecialAbility());
       put("q", actionController -> {
           actionController.doNothing();
           return true;
       });
    }};

    private final Printer printer;
    private final ActionReader actionReader;
    private final LevelController levelController;
    private ActionController actionController;
    private int currentLevel;

    private boolean levelComplete;
    private GameFinishedResult gameResult;

    public CliView(Printer printer, ActionReader actionReader, LevelController levelController) {
        super(printer);

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

        levelController.setView(this);
        this.levelComplete = false;
    }

    private void loadNextLevel() {
        this.currentLevel++;
        this.actionController = this.levelController.loadLevel(this.currentLevel);
    }

    private void act() {
        String nextAction = this.actionReader.nextAction();
        if (!actions.containsKey(nextAction)) {
            this.printer.printLine("Action '" + nextAction + "' is not defined");
        }
        else {
            boolean result = actions.get(nextAction).doAction(this.actionController);
            if (!result) {
                this.printer.printLine("Illegal move or action.");
            }
        }
    }

    public void startGame() {
        this.loadNextLevel();
        if (noLevelsExist()) {
            this.printer.printLine("No levels are present.");
        }
        else {
            this.doGameLoop();
        }
    }

    private boolean noLevelsExist() {
        return this.actionController == null;
    }

    private void doGameLoop() {
        boolean gameFinished = false;
        while (!gameFinished) {
            while (!this.levelComplete) {
                this.act();
                if (!this.levelComplete) {
                    this.printBoard();
                }
            }

            this.levelComplete = false;
            this.loadNextLevel();
            if (this.gameResult != null) {
                switch (this.gameResult) {
                    case Win:
                        this.playerWin();
                        gameFinished = true;
                        break;
                    case Lose:
                        this.playerLose();
                        gameFinished = true;
                        break;
                }
            }
        }
    }

    private void playerWin() {
        this.printer.printLine("Game is finished. You won!");
    }

    private void playerLose() {
        this.printer.printLine("You Lost.");
        this.printBoard();
    }

    private void printBoard() {
        char[][] board = this.levelController.getBoard();
        for (int i = 0; i < board.length; i++) {
            String row = new String(board[i]);
            this.printer.printLine(row);
        }
    }

    @Override
    public void onLevelComplete() {
        this.levelComplete = true;
    }

    @Override
    public void onGameLose() {
        this.gameResult = GameFinishedResult.Lose;
    }

    @Override
    public void onGameWin() {
        this.gameResult = GameFinishedResult.Win;
    }

    private interface PlayerAction {
        boolean doAction(ActionController actionController);
    }
}
