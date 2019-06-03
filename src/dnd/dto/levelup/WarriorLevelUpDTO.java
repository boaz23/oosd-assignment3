package dnd.dto.levelup;

import dnd.View;

public class WarriorLevelUpDTO extends LevelUpDTO {
    @Override
    public String accept(View view) {
        return view.formatString(this);
    }
}
