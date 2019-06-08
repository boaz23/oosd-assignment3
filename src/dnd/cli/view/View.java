package dnd.cli.view;

import dnd.GameEventObserver;
import dnd.dto.levelup.MageLevelUpDTO;
import dnd.dto.levelup.RogueLevelUpDTO;
import dnd.dto.levelup.WarriorLevelUpDTO;
import dnd.dto.units.EnemyDTO;
import dnd.dto.units.MageDTO;
import dnd.dto.units.RogueDTO;
import dnd.dto.units.WarriorDTO;

public interface View extends GameEventObserver {
    void onLevelComplete();
    void onGameLose();
    void onGameWin();

    String formatString(MageDTO mage);
    String formatString(WarriorDTO warrior);
    String formatString(RogueDTO rouge);
    String formatString(EnemyDTO enemy);
    String formatString(MageLevelUpDTO mageLevelUp);
    String formatString(RogueLevelUpDTO rougeLevelUp);
    String formatString(WarriorLevelUpDTO warriorLevelUp);
}
