package dnd.dto.levelup;

import dnd.View;
import dnd.dto.DTO;

public abstract class LevelUpDTO implements DTO {
    public int healthBonus;
    public int attackBonus;
    public int defenseBonus;
}
