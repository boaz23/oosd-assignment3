package dnd.dto.levelup;

import dnd.View;

public class MageLevelUpDTO extends LevelUpDTO {
    public int manaPoolBonus;
    public int spellPowerBonus;

    @Override
    public String accept(View view) {
        return view.formatString(this);
    }
}
