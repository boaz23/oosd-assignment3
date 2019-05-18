package dnd.logic;
;
import dnd.logic.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Level {
    BoardSquare[][] board;

    public List<Unit> getUnitsInRange(Point position, int range, UnitType unitType) {
        int xStart = Math.max(position.getX() - range, 0);
        int xEnd = Math.min(position.getX() + range, board.length);

        int yStart = Math.max(position.getY() - range, 0);
        int yEnd = Math.min(position.getY() + range, board[position.getX()].length);

        List<Unit> unitsInRange = new ArrayList<>();
        for (int x = xStart; x < xEnd; x++) {
            for (int y = yStart; y < yEnd; y++) {
                // ignore the given position
                if (x == position.getX() & y == position.getY()) {
                    continue;
                }
                
                BoardSquare square = board[x][y];
                if (square.getSquareType() == BoardSqaureType.Unit) {
                    Unit unit = (Unit)square;
                    if (unit.getUnitType() == unitType) {
                        unitsInRange.add(unit);
                    }
                }
            }
        }

        return unitsInRange;
    }

    public Player getPlayerInRange(Point position, int range) {
        int xStart = Math.max(position.getX() - range, 0);
        int xEnd = Math.min(position.getX() + range, board.length);

        int yStart = Math.max(position.getY() - range, 0);
        int yEnd = Math.min(position.getY() + range, board[position.getX()].length);

        for (int x = xStart; x < xEnd; x++) {
            for (int y = yStart; y < yEnd; y++) {
                // ignore the given position
                if (x == position.getX() & y == position.getY()) {
                    continue;
                }

                BoardSquare square = board[x][y];
                if (square.getSquareType() == BoardSqaureType.Unit) {
                    Unit unit = (Unit)square;
                    if (unit.getUnitType() == UnitType.Player) {
                        return (Player)unit;
                    }
                }
            }
        }

        return null;
    }
}