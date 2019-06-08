package dnd.dto.levelup;

import dnd.cli.view.View;

public class WarriorLevelUpDTO extends LevelUpDTO {
    @Override
    public String accept(View view) {
        return view.formatString(this);
    }

    @Override
    public WarriorLevelUpDTO clone() {
        WarriorLevelUpDTO warriorLevelUpDTO = new WarriorLevelUpDTO();
        warriorLevelUpDTO.healthBonus = healthBonus;
        warriorLevelUpDTO.attackBonus = attackBonus;
        warriorLevelUpDTO.defenseBonus = defenseBonus;
        return warriorLevelUpDTO;
    }
}
