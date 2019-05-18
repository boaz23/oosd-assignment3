package dnd.logic;

import java.util.List;

public interface UnitsInRangeFinder {
    List<Unit> findUnitsInRange(Point position, int range, UnitType unitType);
}
