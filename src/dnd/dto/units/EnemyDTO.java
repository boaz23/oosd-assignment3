package dnd.dto.units;

import dnd.cli.view.View;

public class EnemyDTO extends UnitDTO {
    @Override
    public String accept(View view) {
        return view.formatString(this);
    }
}
