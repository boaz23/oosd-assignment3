package dnd.dto.levelup;

import dnd.cli.view.View;

public class RogueLevelUpDTO extends LevelUpDTO {
    @Override
    public String accept(View view) {
        return view.formatString(this);
    }

    @Override
    public RogueLevelUpDTO clone() {
        RogueLevelUpDTO rogueLevelUpDTO = new RogueLevelUpDTO();
        rogueLevelUpDTO.healthBonus = healthBonus;
        rogueLevelUpDTO.attackBonus = attackBonus;
        rogueLevelUpDTO.defenseBonus = defenseBonus;
        return rogueLevelUpDTO;
    }
}
