package dnd;

import dnd.dto.levelup.*;
import dnd.dto.units.*;

public interface View {
    void onCastingSpecialAbility(PlayerDTO player);
    void onExpGain(PlayerDTO player);
    void onLevelUp(PlayerDTO player);

    void onDeath(PlayerDTO player);
    void onDeath(EnemyDTO enemy);

    void onPlayerEngage(PlayerDTO player, EnemyDTO enemy);
    void onEnemyEngage(EnemyDTO enemy, PlayerDTO player);
    void onHit(UnitDTO attacker, UnitDTO defender, int damage);
    void onAttackPointsRoll(UnitDTO unit, int attackPoints);
    void onDefensePointsRoll(UnitDTO unit, int defensePoints);

    String formatString(MageDTO mage);
    String formatString(WarriorDTO warrior);
    String formatString(RougeDTO rouge);
    String formatString(EnemyDTO enemy);
    String formatString(MageLevelUpDTO mageLevelUp);
    String formatString(RougeLevelUpDTO rougeLevelUp);
    String formatString(WarriorLevelUpDTO warriorLevelUp);
}
