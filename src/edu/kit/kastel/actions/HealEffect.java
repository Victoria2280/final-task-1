package edu.kit.kastel.actions;

import edu.kit.kastel.Element;
import edu.kit.kastel.monsters.Monster;
import edu.kit.kastel.monsters.TargetMonster;
import edu.kit.kastel.reader.CompetitionInterrupter;

import java.util.ListIterator;
/**
 * The HealEffect class represents a healing effect in the game.
 * It extends DamageHealCommonEffect and allows a monster to recover health
 * based on different strength calculation methods.
 * @author uljyv
 * @version 1.0
 */
public final class HealEffect extends DamageHealCommonEffect {

    private TargetMonster targetMonster;

    private HealEffect(Strength strength, int hitRate, TargetMonster targetMonster) {
        super(strength, hitRate);
        this.targetMonster = targetMonster;
    }
    /**
     * Reads a HealEffect from a list iterator of strings.
     *
     * @param lineIterator The iterator containing effect details.
     * @return A new HealEffect instance if parsing is successful, otherwise null.
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
            return new HealEffect(new Strength(strengthType, strengthValue), hitRate, target);
        } catch (NumberFormatException nfe) {
            return  null;
        }
    }

    /**
     * Calculates the amount of health restored by this healing effect.
     *
     * @param user The monster using the healing effect.
     * @param target The target monster receiving the healing.
     * @param actionElement The element associated with the action.
     * @return The amount of HP restored.
     * @throws CompetitionInterrupter If the competition is interrupted.
     */
    public int getHeal(Monster user, Monster target, Element actionElement) throws CompetitionInterrupter {
        switch (strength.getType()) {
            case ABS:
                return getAbs();
            case REL:
                return (user.getMonsterBase().getMaxHP() * strength.getValue()) / 100;
            case BASE:
                return getBaseDamage(user, target, actionElement);
            default: return 0;
        }
    }

    @Override
    public boolean applyEffect(Monster user, Monster target, Element actionElement, boolean isFirstInAction) throws CompetitionInterrupter {

        Monster monster = targetMonster.getMonster(user, target);

        if (!isHit(user, monster)) {
            return false;
        }

        monster.heal(getHeal(user, monster, actionElement));
        return true;
    }

    @Override
    public String getContext() {
        return "heal hit";
    }

    @Override
    public boolean needTarget() {
        return targetMonster == TargetMonster.TARGET;
    }
}