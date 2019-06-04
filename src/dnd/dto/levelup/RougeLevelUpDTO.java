package dnd.dto.levelup;

import dnd.cli.view.View;

public class RougeLevelUpDTO extends LevelUpDTO {
    @Override
    public String accept(View view) {
        return view.formatString(this);
    }

    @Override
    protected LevelUpDTO clone() {
        RougeLevelUpDTO rougeLevelUpDTO = new RougeLevelUpDTO();
        rougeLevelUpDTO.healthBonus = this.healthBonus;
        rougeLevelUpDTO.attackBonus = this.attackBonus;
        rougeLevelUpDTO.defenseBonus = this.defenseBonus;
        return rougeLevelUpDTO;
    }
}
