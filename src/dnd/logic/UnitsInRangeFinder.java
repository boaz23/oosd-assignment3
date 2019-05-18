package dnd.logic;

import java.util.List;

public interface UnitInRangeFinder {
    List<Unit> findUnitsInRange(Point position, int range, UnitType unitType);
}
