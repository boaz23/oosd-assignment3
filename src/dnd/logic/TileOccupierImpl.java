package dnd.logic;

import java.util.HashSet;
import java.util.Set;

public class TileOccupierImpl implements TileOccupier {
    private Set<TileProperty> properties;

    public TileOccupierImpl() {
        this.properties = new HashSet<>();
    }

    protected void addProperty(TileProperty property) {
        if (property == null) {
            throw new IllegalArgumentException("property is null.");
        }

        this.properties.add(property);
    }

    @Override
    public ReadOnlySet<TileProperty> getProperties() {
        return new ReadOnlySetImpl<>(this.properties);
    }
}
