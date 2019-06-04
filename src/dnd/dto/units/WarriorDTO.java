package dnd.dto.units;

import dnd.cli.view.View;

public class WarriorDTO extends PlayerDTO {
    public int abilityCooldown;
    public int remaining;

    @Override
    public String accept(View view) {
        return view.formatString(this);
    }
}
