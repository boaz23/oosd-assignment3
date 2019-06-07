package dnd.cli.view;

import dnd.cli.printer.Printer;
import dnd.dto.levelup.LevelUpDTO;
import dnd.dto.levelup.MageLevelUpDTO;
import dnd.dto.levelup.RougeLevelUpDTO;
import dnd.dto.levelup.WarriorLevelUpDTO;
import dnd.dto.units.*;

abstract class PrintEventsView implements View {
    private Printer printer;

    public PrintEventsView(Printer printer) {
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

    protected String resolveFormatString(PlayerDTO playerDTO) {
        return playerDTO.accept(this);
    }

    protected String resolveFormatString(LevelUpDTO levelUp) {
        return levelUp.accept(this);
    }

    private String formatString(UnitDTO unit) {
        String unitInfo = unit.name + "\t\tHealth: " + unit.currentHealth + "\t\tAttack damage: " + unit.attack + "\t\tDefense: " + unit.defense;
        return unitInfo;
    }

    private String formatString(PlayerDTO playerDTO) {
        String playerInfo = formatString((UnitDTO)playerDTO) + "\n";
        playerInfo += "\t\tLevel: " + playerDTO.level + "\t\tExperience: " + playerDTO.experience + "/" + playerDTO.totalExperienceToNextLevel;
        return playerInfo;
    }

    @Override
    public String formatString(MageDTO mage) {
        String mageInfo = formatString((PlayerDTO)mage);
        mageInfo += "\t\tSpellPower: " + mage.spellPower + "\t\tMana: " + mage.currentMana + "/" + mage.manaPool;
        return mageInfo;
    }

    @Override
    public String formatString(WarriorDTO warrior) {
        String warriorInfo = formatString((PlayerDTO)warrior);
        warriorInfo += "\t\tAbility cooldown: " + warrior.abilityCooldown + "\t\tRemaining: " + warrior.remaining;
        return warriorInfo;
    }

    @Override
    public String formatString(RogueDTO rouge) {
        String rougeInfo = formatString((PlayerDTO)rouge);
        rougeInfo += "\t\tEnergy: " + rouge.currentEnergy + "/" + rouge.maxEnergy;
        return rougeInfo;
    }

    @Override
    public String formatString(EnemyDTO enemy) {
        return formatString((UnitDTO)enemy);
    }

    public String formatString(LevelUpDTO levelUpDTO) {
        String levelUpInfo = "Level up: +" + levelUpDTO.healthBonus + " Health, +" + levelUpDTO.attackBonus + " Attack, +" + levelUpDTO.defenseBonus + " Defense";
        return levelUpInfo;
    }

    @Override
    public String formatString(MageLevelUpDTO mageLevelUp) {
        String mageLevelUpInfo = formatString((LevelUpDTO)mageLevelUp) +
            "\n\t\t+" + mageLevelUp.manaPoolBonus + " maximum mana, +"
            + mageLevelUp.spellPowerBonus + " spell power";
        return mageLevelUpInfo;
    }

    @Override
    public String formatString(RougeLevelUpDTO rougeLevelUp) {
        return formatString((LevelUpDTO)rougeLevelUp);
    }

    @Override
    public String formatString(WarriorLevelUpDTO warriorLevelUp) {
        return formatString((LevelUpDTO)warriorLevelUp);
    }
}
