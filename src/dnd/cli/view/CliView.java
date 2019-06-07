package dnd.cli.view;

import dnd.GameException;
import dnd.cli.GameFinishedResult;
import dnd.cli.action_reader.ActionReader;
import dnd.cli.printer.Printer;
import dnd.controllers.ActionController;
import dnd.controllers.LevelController;
import dnd.dto.units.PlayerDTO;

import java.util.HashMap;

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

    public void startGame() throws GameException {
        this.selectPlayerAndShowControls();
        this.startLevelsPlay();
    }

    private void selectPlayerAndShowControls() {
        this.showPlayerSelectMenu();
        PlayerDTO playerChoise = this.choosePlayer();
        this.printPlayerChoise(playerChoise);
        this.printControls();
    }

    private void showPlayerSelectMenu() {
        this.printer.printLine("Select player:");
        PlayerDTO[] playerChoises = this.levelController.getPlayerChoises();
        for (int i = 0; i < playerChoises.length; i++) {
            PlayerDTO playerChoise = playerChoises[i];
            this.printer.print((i + 1) + ". ");
            this.printer.printLine(this.resolveFormatString(playerChoise));
        }
    }

    private PlayerDTO choosePlayer() {
        PlayerDTO playerChoise = this.choosePlayerCore();
        while (playerChoise == null) {
            this.printer.printLine("Select Player:");
            playerChoise = this.choosePlayerCore();
        }

        return playerChoise;
    }

    private PlayerDTO choosePlayerCore() {
        try {
            int choise = Integer.parseInt(this.actionReader.nextAction());
            return this.levelController.choosePlayer(choise);
        }
        catch (NumberFormatException ignored) {
        }

        return null;
    }

    private void printPlayerChoise(PlayerDTO playerChoise) {
        this.printer.printLine("You have selected:");
        this.printer.printLine(this.resolveFormatString(playerChoise));
    }

    private void printControls() {
        this.printer.printLine("Use w/s/a/d to move.");
        this.printer.printLine("Use e for special ability or q to pass.");
    }

    private void startLevelsPlay() throws GameException {
        this.loadNextLevel();
        if (noLevelsExist()) {
            this.printer.printLine("No levels are present. exiting...");
        }
        else {
            this.doGameLoop();
        }
    }

    private void loadNextLevel() {
        this.currentLevel++;
        this.actionController = this.levelController.loadLevel(this.currentLevel);
    }

    private boolean noLevelsExist() {
        return this.actionController == null;
    }

    private void doGameLoop() throws GameException {
        boolean gameFinished = false;
        while (!gameFinished) {
            while (!this.levelComplete) {
                this.printBoard();
                this.printPlayerStats();
                this.act();
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

    private void printBoard() {
        char[][] board = this.levelController.getBoard();
        for (int i = 0; i < board.length; i++) {
            String row = new String(board[i]);
            this.printer.printLine(row);
        }
    }

    private void printPlayerStats() {
        this.printer.printLine("\n");
        this.printer.printLine(this.resolveFormatString(this.levelController.getPlayer()));
    }

    private void act() throws GameException {
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

    private void playerWin() {
        this.printer.printLine("Game is finished. You won!");
    }

    private void playerLose() {
        this.printer.printLine("You Lost.");
        this.printBoard();
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
        boolean doAction(ActionController actionController) throws GameException;
    }
}
