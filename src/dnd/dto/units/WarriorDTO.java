package dnd.dto.units;

import dnd.View;

public class WarriorDTO extends PlayerDTO {
    @Override
    public String accept(View view) {
        return view.formatString(this);
    }
}
