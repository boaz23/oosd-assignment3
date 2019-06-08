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
    public MageLevelUpDTO clone() {
        MageLevelUpDTO mageLevelUpDTO = new MageLevelUpDTO();
        mageLevelUpDTO.healthBonus = healthBonus;
        mageLevelUpDTO.attackBonus = attackBonus;
        mageLevelUpDTO.defenseBonus = defenseBonus;
        mageLevelUpDTO.manaPoolBonus = manaPoolBonus;
        mageLevelUpDTO.spellPowerBonus = spellPowerBonus;
        return mageLevelUpDTO;
    }
}
