package dnd.dto.levelup;

import dnd.cli.view.View;

public class MageLevelUpDTO extends LevelUpDTO {
    public int manaPoolBonus;
    public int spellPowerBonus;

    @Override
    public String accept(View view) {
        return view.formatString(this);
    }

    @Override
    protected LevelUpDTO clone() {
        MageLevelUpDTO mageLevelUpDTO = new MageLevelUpDTO();
        mageLevelUpDTO.healthBonus = this.healthBonus;
        mageLevelUpDTO.attackBonus = this.attackBonus;
        mageLevelUpDTO.defenseBonus = this.defenseBonus;
        mageLevelUpDTO.manaPoolBonus = this.manaPoolBonus;
        mageLevelUpDTO.spellPowerBonus = this.spellPowerBonus;
        return mageLevelUpDTO;
    }
}
