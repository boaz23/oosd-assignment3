package dnd.logic.board;

import dnd.logic.Point;
import dnd.logic.tileOccupiers.TileOccupier;
import dnd.logic.tileOccupiers.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BoardImpl implements Board {
    BoardSquare[][] board;

    @Override
    public List<TileOccupier> findTileOccupiers(Point position, int range, Set<TileProperty> properties) {
        if (position == null) {
            throw new IllegalArgumentException("position is null.");
        }
        if (range <= 0) {
            throw new IllegalArgumentException("range must be a positive number.");
        }
        if (properties == null) {
            throw new IllegalArgumentException("properties is null.");
        }


        int xStart = Math.max(position.getX() - range, 0);
        int xEnd = Math.min(position.getX() + range, board.length);

        int yStart = Math.max(position.getY() - range, 0);
        int yEnd = Math.min(position.getY() + range, board[position.getX()].length);

        List<TileOccupier> tilesInRange = new ArrayList<>();
        for (int x = xStart; x < xEnd; x++) {
            for (int y = yStart; y < yEnd; y++) {
                Point point = new Point(x, y);

                // ignore points which are too far away and the given position
                if (point.equals(position) | Point.distance(point, position) >= range) {
                    continue;
                }

                BoardSquare square = board[x][y];
                TileOccupier tileOccupier = square.getTileOccupier();
                tilesInRange.add(tileOccupier);
            }
        }

        return tilesInRange;
    }

    @Override
    public boolean move(Unit unit, Point targetPosition) {
        if (unit == null) {
            throw new IllegalArgumentException("unit is null.");
        }
        if (targetPosition == null) {
            throw new IllegalArgumentException("position is null.");
        }

        if (targetPosition.getX() >= board.length ||
            targetPosition.getY() >= board[targetPosition.getX()].length) {
            return false;
        }

        BoardSquare square = board[targetPosition.getX()][targetPosition.getY()];
        TileOccupier targetTileOccupier = square.getTileOccupier();


        if (targetTileOccupier.getProperties().contains(TileProperty.Free)) {
            // switch between the units
            Point currentPosition = unit.getPosition();
            board[currentPosition.getX()][currentPosition.getY()].setTileOccupier(targetTileOccupier);
            board[targetPosition.getX()][targetPosition.getY()].setTileOccupier(unit);
        }
        else if ((unit.getProperties().contains(TileProperty.Player) & targetTileOccupier.getProperties().contains(TileProperty.Enemy)) |
                (unit.getProperties().contains(TileProperty.Monster) & targetTileOccupier.getProperties().contains(TileProperty.Player))) {
            // combat
            // what happens when a player engages a monster
        }
        else {
            return false;
        }
    }
}