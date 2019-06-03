package dnd.dto.units;

import dnd.View;

public class RougeDTO extends PlayerDTO {
    public int currentEnergy;
    public int maxEnergy;

    @Override
    public String accept(View view) {
        return view.formatString(this);
    }
}
