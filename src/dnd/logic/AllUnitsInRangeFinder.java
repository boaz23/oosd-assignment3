package dnd.logic;

import java.util.List;

public class AllUnitsInRangeFinder implements UnitsInRangeFinder {
    protected Level level;

    public AllUnitsInRangeFinder(Level level) {
        this.level = level;
    }

    @Override
    public List<Unit> findUnitsInRange(Point position, int range, UnitType unitType) {
        return this.level.getUnitsInRange(position, range, unitType);
    }
}
