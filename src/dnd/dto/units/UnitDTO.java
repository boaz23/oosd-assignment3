package dnd.dto.units;

import dnd.View;
import dnd.dto.DTO;

public abstract class UnitDTO implements DTO {
    public String name;
    public int healthPool;
    public int currentHealth;
    public int attack;
    public int defense;
}
