package dnd.dto.levelup;

import dnd.cli.view.View;

public class WarriorLevelUpDTO extends LevelUpDTO {
    @Override
    public String accept(View view) {
        return view.formatString(this);
    }

    @Override
    public LevelUpDTO clone() {
        WarriorLevelUpDTO warriorLevelUpDTO = new WarriorLevelUpDTO();
        warriorLevelUpDTO.healthBonus = this.healthBonus;
        warriorLevelUpDTO.attackBonus = this.attackBonus;
        warriorLevelUpDTO.defenseBonus = this.defenseBonus;
        return warriorLevelUpDTO;
    }
}
