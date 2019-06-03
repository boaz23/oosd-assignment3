package dnd.cli;

import dnd.View;
import dnd.dto.levelup.MageLevelUpDTO;
import dnd.dto.levelup.RougeLevelUpDTO;
import dnd.dto.levelup.WarriorLevelUpDTO;
import dnd.dto.units.*;

// TODO: implement message system between the logic and the view (for printing events)
public class CliEventsView implements View {
    @Override
    public void onCastingSpecialAbility(PlayerDTO player) {

    }

    @Override
    public void onExpGain(PlayerDTO player) {

    }

    @Override
    public void onLevelUp(PlayerDTO player) {

    }

    @Override
    public void onDeath(PlayerDTO player) {

    }

    @Override
    public void onDeath(EnemyDTO enemy) {

    }

    @Override
    public void onPlayerEngage(PlayerDTO player, EnemyDTO enemy) {
    }

    @Override
    public void onEnemyEngage(EnemyDTO enemy, PlayerDTO player) {

    }

    @Override
    public void onHit(UnitDTO attacker, UnitDTO defender, int damage) {

    }

    @Override
    public void onAttackPointsRoll(UnitDTO unit, int attackPoints) {

    }

    @Override
    public void onDefensePointsRoll(UnitDTO unit, int defensePoints) {

    }

    @Override
    public String formatString(MageDTO mage) {
        return null;
    }

    @Override
    public String formatString(WarriorDTO warrior) {
        return null;
    }

    @Override
    public String formatString(RougeDTO rouge) {
        return null;
    }

    @Override
    public String formatString(EnemyDTO enemy) {
        return null;
    }

    @Override
    public String formatString(MageLevelUpDTO mageLevelUp) {
        return null;
    }

    @Override
    public String formatString(RougeLevelUpDTO rougeLevelUp) {
        return null;
    }

    @Override
    public String formatString(WarriorLevelUpDTO warriorLevelUp) {
        return null;
    }
}
