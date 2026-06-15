package edu.kit.kastel.actions;

import edu.kit.kastel.Element;
import edu.kit.kastel.monsters.Monster;
import edu.kit.kastel.monsters.TargetMonster;
import edu.kit.kastel.reader.CompetitionInterrupter;

import java.util.ListIterator;
/**
 * The DamageEffect class represents an effect that deals damage to a target monster.
 * It extends DamageHealCommonEffect and determines the damage based on the specified strength type.
 * The damage can be absolute, relative to the user's attack, or calculated using base values.
 * This effect also considers target protection and hit rate mechanics.
 * @author uljyv
 * @version 1.0
 */
public final class DamageEffect extends DamageHealCommonEffect {

    private TargetMonster targetMonster;

    private DamageEffect(Strength strength, int hitRate, TargetMonster targetMonster) {
        super(strength, hitRate);
        this.targetMonster = targetMonster;
    }
    /**
     * Calculates the amount of damage dealt to the target based on the strength type.
     *
     * @param user The monster using the action.
     * @param target The monster receiving the damage.
     * @param actionElement The element associated with the action, which may influence damage calculation.
     * @return The calculated damage value.
     * @throws CompetitionInterrupter If the competition is interrupted.
     */
    public int getDamage(Monster user, Monster target, Element actionElement) throws CompetitionInterrupter {
        switch (strength.getType()) {
            case ABS:
                return getAbs();
            case BASE:
                return getBaseDamage(user, target, actionElement);
            case REL:
                return (int) Math.ceil((double) (user.getMonsterBase().getMaxHP() * strength.getValue()) / 100);
            default: return 0;
        }
    }
    /**
     * Reads and parses a DamageEffect from a given iterator of strings.
     * The input should specify the target monster, strength type, strength value, and hit rate.
     *
     * @param lineIterator A ListIterator of strings representing lines of effect data.
     * @return A DamageEffect object if the input data is valid; otherwise null.
     */
    public static Effect readEffect(ListIterator<String> lineIterator) {
        if (!lineIterator.hasNext()) {
            return null;
        }

        String line = lineIterator.next().trim();
        String[] parts = line.split(" ");

        if (parts.length != 5) {
            return null;
        }

        TargetMonster target = TargetMonster.getTargetMonster(parts[1]);
        if (target == null) {
            return null;
        }

        StrengthType strengthType = StrengthType.getStrengthType(parts[2]);
        if (strengthType == null) {
            return null;
        }

        try {
            int strengthValue = Integer.parseInt(parts[3]);
            int hitRate = Integer.parseInt(parts[4]);
            return new DamageEffect(new Strength(strengthType, strengthValue), hitRate, target);
        } catch (NumberFormatException nfe) {
            return  null;
        }
    }

    @Override
    public boolean applyEffect(Monster user, Monster target, Element actionElement, boolean isFirstInAction) throws CompetitionInterrupter {

        Monster monster = targetMonster.getMonster(user, target);

        if (targetMonster != TargetMonster.USER) {
            if (monster.getProtectTarget() == ProtectTarget.HEALTH) {
                System.out.println(monster.getName() + " is protected and takes no damage!");
                return true;
            }
        }

        if (!isHit(user, monster)) {
            return false;
        }

        monster.takeDamage(getDamage(user, monster, actionElement), "");
        return true;
    }

    @Override
    public String getContext() {
        return "attack hit";
    }

    @Override
    public String getDamageStr() {
        return strength.shortStringr();
    }

    @Override
    public boolean needTarget() {
        return true;
    }
}