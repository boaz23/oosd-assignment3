package dnd.cli.view;

import dnd.cli.printer.Printer;
import dnd.dto.DTO;
import dnd.dto.levelup.LevelUpDTO;
import dnd.dto.levelup.MageLevelUpDTO;
import dnd.dto.levelup.RogueLevelUpDTO;
import dnd.dto.levelup.WarriorLevelUpDTO;
import dnd.dto.units.*;

/**
 * Responsible for printing the various game events that happen
 */
abstract class PrintEventsView implements View {
    private final Printer printer;

    PrintEventsView(Printer printer) {
        if (printer == null) {
            throw new IllegalArgumentException("printer is null.");
        }
        this.printer = printer;
    }

    @Override
    public void onCastingSpecialAbility(PlayerDTO player) {
        String output = player.name + " cast " + player.specialAbilityName + ".";
        printer.printLine(output);
    }

    @Override
    public void onExpGain(PlayerDTO player, int experienceGained) {
        String output = player.name + " gained " + experienceGained + " experience!";
        printer.printLine(output);
    }

    @Override
    public void onLevelUp(LevelUpDTO levelUp) {
        String output = resolveFormatString(levelUp);
        printer.printLine(output);
    }

    @Override
    public void onPlayerDeath(PlayerDTO player) {
        String output = player.name + " died.";
        printer.printLine(output);
    }

    @Override
    public void onEnemyDeath(EnemyDTO enemy) {
        String output = enemy.name + " died. ";
        printer.print(output);
    }

    @Override
    public void onPlayerEngage(PlayerDTO player, EnemyDTO enemy) {
        String output = player.name + " engaged in battle with " + enemy.name + ":\n"
            + resolveFormatString(player) + "\n"
            + formatString(enemy);
        printer.printLine(output);
    }

    @Override
    public void onEnemyEngage(EnemyDTO enemy, PlayerDTO player) {
        String output = enemy.name + " engaged in battle with " + player.name + ":\n"
            + formatString(enemy) + "\n"
            + resolveFormatString(player);
        printer.printLine(output);
    }

    @Override
    public void onAttackPointsRoll(UnitDTO unit, int attackPoints) {
        String output = unit.name + " rolled " + attackPoints + " attack points.";
        printer.printLine(output);
    }

    @Override
    public void onDefensePointsRoll(UnitDTO unit, int defensePoints) {
        String output = unit.name + " rolled " + defensePoints + " defense points.";
        printer.printLine(output);
    }

    @Override
    public void onHit(UnitDTO attacker, UnitDTO defender, int damage) {
        String output = attacker.name + " hit " + defender.name + " for " + damage + " damage.";
        printer.printLine(output);
    }

    String resolveFormatString(DTO dto) {
        return dto.accept(this);
    }

    private String formatUnitString(UnitDTO unit) {
        return unit.name + "\t\tHealth: " + unit.currentHealth +
            "\t\tAttack damage: " + unit.attack +
            "\t\tDefense: " + unit.defense;
    }

    private String formatPlayerString(PlayerDTO playerDTO) {
        String playerInfo = formatUnitString(playerDTO) + "\n";
        playerInfo += "\t\tLevel: " + playerDTO.level + "\t\tExperience: " + playerDTO.experience + "/" + playerDTO.totalExperienceToNextLevel;
        return playerInfo;
    }

    @Override
    public String formatString(MageDTO mage) {
        String mageInfo = formatPlayerString(mage);
        mageInfo += "\t\tSpellPower: " + mage.spellPower + "\t\tMana: " + mage.currentMana + "/" + mage.manaPool;
        return mageInfo;
    }

    @Override
    public String formatString(WarriorDTO warrior) {
        String warriorInfo = formatPlayerString(warrior);
        warriorInfo += "\t\tAbility cooldown: " + warrior.abilityCooldown + "\t\tRemaining: " + warrior.remaining;
        return warriorInfo;
    }

    @Override
    public String formatString(RogueDTO rogue) {
        String rougeInfo = formatPlayerString(rogue);
        rougeInfo += "\t\tEnergy: " + rogue.currentEnergy + "/" + rogue.maxEnergy;
        return rougeInfo;
    }

    @Override
    public String formatString(EnemyDTO enemy) {
        return formatUnitString(enemy);
    }

    String formatLevelUpString(LevelUpDTO levelUp) {
        return "Level up: +" +
            levelUp.healthBonus + " Health, +" +
            levelUp.attackBonus + " Attack, +" +
            levelUp.defenseBonus + " Defense";
    }

    @Override
    public String formatString(MageLevelUpDTO mageLevelUp) {
        return formatLevelUpString(mageLevelUp) +
            "\n\t\t+" + mageLevelUp.manaPoolBonus + " maximum mana, +"
            + mageLevelUp.spellPowerBonus + " spell power";
    }

    @Override
    public String formatString(RogueLevelUpDTO rogueLevelUp) {
        return formatLevelUpString(rogueLevelUp);
    }

    @Override
    public String formatString(WarriorLevelUpDTO warriorLevelUp) {
        return formatLevelUpString(warriorLevelUp);
    }
}
