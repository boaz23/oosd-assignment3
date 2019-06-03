package dnd.dto.units;

import dnd.View;

public class EnemyDTO extends UnitDTO {
    @Override
    public String accept(View view) {
        return view.formatString(this);
    }
}
