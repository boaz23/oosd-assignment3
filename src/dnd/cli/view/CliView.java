package dnd.cli.view;

import dnd.GameException;
import dnd.cli.GameFinishedResult;
import dnd.cli.action_reader.ActionReader;
import dnd.cli.printer.Printer;
import dnd.controllers.ActionController;
import dnd.controllers.LevelController;
import dnd.dto.units.PlayerDTO;

import java.util.HashMap;
import java.util.Map;

public class CliView extends PrintEventsView {
    private static final Map<String, PlayerAction> actions = new HashMap<String, PlayerAction>() {{
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
        currentLevel = 0;

        levelController.setView(this);
        levelComplete = false;
    }

    public void startGame() throws GameException {
        selectPlayerAndShowControls();
        startLevelsPlay();
    }

    private void selectPlayerAndShowControls() {
        showPlayerSelectMenu();
        PlayerDTO playerChoice = choosePlayer();
        printPlayerChoice(playerChoice);
        printControls();
    }

    private void showPlayerSelectMenu() {
        printer.printLine("Select player:");
        PlayerDTO[] playerChoices = levelController.getPlayerChoices();
        for (int i = 0; i < playerChoices.length; i++) {
            PlayerDTO playerChoice = playerChoices[i];
            printer.print((i + 1) + ". ");
            printer.printLine(resolveFormatString(playerChoice));
        }
    }

    private PlayerDTO choosePlayer() {
        PlayerDTO playerChoice = choosePlayerCore();
        while (playerChoice == null) {
            printer.printLine("Invalid player selection.");
            playerChoice = choosePlayerCore();
        }

        return playerChoice;
    }

    private PlayerDTO choosePlayerCore() {
        try {
            int choice = Integer.parseInt(actionReader.nextAction());
            return levelController.choosePlayer(choice);
        }
        catch (NumberFormatException ignored) {
        }

        return null;
    }

    private void printPlayerChoice(PlayerDTO playerChoice) {
        printer.printLine("You have selected:");
        printer.printLine(resolveFormatString(playerChoice));
    }

    private void printControls() {
        printer.printLine("Use w/s/a/d to move.");
        printer.printLine("Use e for special ability or q to pass.");
    }

    private void startLevelsPlay() throws GameException {
        loadNextLevel();
        if (noLevelsExist()) {
            printer.printLine("No levels are present. exiting...");
        }
        else {
            doGameLoop();
        }
    }

    private void loadNextLevel() {
        currentLevel++;
        actionController = levelController.loadLevel(currentLevel);
    }

    private boolean noLevelsExist() {
        return actionController == null;
    }

    private void doGameLoop() throws GameException {
        boolean gameFinished = false;
        while (!gameFinished) {
            while (!levelComplete && gameResult == null) {
                printBoard();
                printPlayerStats();
                while (!act()) { }
            }

            levelComplete = false;
            if (gameResult == null) {
                loadNextLevel();
            }

            // may have been changed by loading the next level
            if (gameResult != null) {
                switch (gameResult) {
                    case Win:
                        playerWin();
                        gameFinished = true;
                        break;
                    case Lose:
                        playerLose();
                        gameFinished = true;
                        break;
                }
            }
        }
    }

    private void printBoard() {
        char[][] board = levelController.getBoard();
        for (char[] chars : board) {
            String row = new String(chars);
            printer.printLine(row);
        }
    }

    private void printPlayerStats() {
        printer.printLine("\n");
        printer.printLine(resolveFormatString(levelController.getPlayer()));
    }

    private boolean act() throws GameException {
        boolean isActionValid;
        String nextAction = actionReader.nextAction();
        if (actions.containsKey(nextAction)) {
            isActionValid = actions.get(nextAction).doAction(actionController);
            if (!isActionValid) {
                printer.printLine("Illegal move or action.");
            }
        }
        else {
            isActionValid = false;
            printer.printLine("Action '" + nextAction + "' is not defined");
        }

        return isActionValid;
    }

    private void playerWin() {
        printer.printLine("Game is finished. You won!");
    }

    private void playerLose() {
        printer.printLine("You Lost.");
        printBoard();
    }

    @Override
    public void onLevelComplete() {
        levelComplete = true;
    }

    @Override
    public void onGameLose() {
        gameResult = GameFinishedResult.Lose;
    }

    @Override
    public void onGameWin() {
        gameResult = GameFinishedResult.Win;
    }

    private interface PlayerAction {
        boolean doAction(ActionController actionController) throws GameException;
    }
}
