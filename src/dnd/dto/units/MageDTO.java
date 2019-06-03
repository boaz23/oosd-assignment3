package dnd.dto.units;

import dnd.View;

public class MageDTO extends PlayerDTO {
    public int spellPower;
    public int manaPool;
    public int currentMana;

    @Override
    public String accept(View view) {
        return view.formatString(this);
    }
}
