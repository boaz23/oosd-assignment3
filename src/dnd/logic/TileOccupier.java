package dnd.logic;

import dnd.ReadOnlySet;

public interface TileOccupier {
    ReadOnlySet<TileProperty> getProperties();
}
