package dnd.cli.view;

import dnd.GameEventObserver;
import dnd.dto.levelup.*;
import dnd.dto.units.*;

public interface View extends GameEventObserver {
    void onLevelComplete();
    void onGameLose();
    void onGameWin();

    String formatString(MageDTO mage);
    String formatString(WarriorDTO warrior);
    String formatString(RougeDTO rouge);
    String formatString(EnemyDTO enemy);
    String formatString(MageLevelUpDTO mageLevelUp);
    String formatString(RougeLevelUpDTO rougeLevelUp);
    String formatString(WarriorLevelUpDTO warriorLevelUp);
}
