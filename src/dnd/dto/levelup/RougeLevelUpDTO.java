package dnd.dto.levelup;

import dnd.cli.view.View;

public class RougeLevelUpDTO extends LevelUpDTO {
    @Override
    public String accept(View view) {
        return view.formatString(this);
    }

    @Override
    public LevelUpDTO clone() {
        RougeLevelUpDTO rougeLevelUpDTO = new RougeLevelUpDTO();
        rougeLevelUpDTO.healthBonus = healthBonus;
        rougeLevelUpDTO.attackBonus = attackBonus;
        rougeLevelUpDTO.defenseBonus = defenseBonus;
        return rougeLevelUpDTO;
    }
}
