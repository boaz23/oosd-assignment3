package dnd.cli.view;

import dnd.dto.levelup.LevelUpDTO;
import dnd.dto.units.EnemyDTO;
import dnd.dto.units.PlayerDTO;
import dnd.dto.units.UnitDTO;

public interface GameEventObserver {
    void onCastingSpecialAbility(PlayerDTO player);
    void onExpGain(PlayerDTO player, int experienceGained);
    void onLevelUp(LevelUpDTO levelUp);

    void onPlayerDeath(PlayerDTO player);
    void onEnemyDeath(EnemyDTO enemy);

    void onPlayerEngage(PlayerDTO player, EnemyDTO enemy);
    void onEnemyEngage(EnemyDTO enemy, PlayerDTO player);
    void onHit(UnitDTO attacker, UnitDTO defender, int damage);
    void onAttackPointsRoll(UnitDTO unit, int attackPoints);
    void onDefensePointsRoll(UnitDTO unit, int defensePoints);
}
