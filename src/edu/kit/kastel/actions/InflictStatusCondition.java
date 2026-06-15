package edu.kit.kastel.actions;

import edu.kit.kastel.Element;
import edu.kit.kastel.monsters.Monster;
import edu.kit.kastel.monsters.StatusCondition;
import edu.kit.kastel.monsters.TargetMonster;
import edu.kit.kastel.reader.CompetitionInterrupter;

import java.util.ListIterator;
/**
 * The InflictStatusCondition class represents an effect that applies a status condition to a target monster.
 * This effect has a hit rate, meaning it may fail to apply under certain conditions.
 * It extends EffectHasHitRate.
 * @author uljyv
 * @version 1.0
 */
public final class InflictStatusCondition extends EffectHasHitRate {

    private StatusCondition condition;
    private TargetMonster targetMonster;

    private InflictStatusCondition(StatusCondition condition, int hitRate, TargetMonster targetMonster) {
        super(hitRate);
        this.condition = condition;
        this.targetMonster = targetMonster;
    }
    /**
     * Reads an InflictStatusCondition effect from a given list iterator.
     * Parses the effect parameters and returns a new instance.
     *
     * @param lineIterator The iterator containing effect details.
     * @return A new InflictStatusCondition instance if parsing succeeds, otherwise null.
     */
    public static Effect readEffect(ListIterator<String> lineIterator) {
        if (!lineIterator.hasNext()) {
            return null;
        }

        String line = lineIterator.next().trim();
        String[] parts = line.split(" ");
        if (parts.length != 4) {
            return null;
        }

        TargetMonster target = TargetMonster.getTargetMonster(parts[1]);
        if (target == null) {
            return null;
        }

        StatusCondition statusCondition = StatusCondition.getStatusCondition(parts[2]);
        if (statusCondition == null) {
            return null;
        }

        try {
            int hitRate = Integer.parseInt(parts[3]);
            return new InflictStatusCondition(statusCondition, hitRate, target);
        } catch (NumberFormatException nfe) {
            return null;
        }
    }

    @Override
    public boolean applyEffect(Monster user, Monster target, Element actionElement, boolean isFirstInAction) throws CompetitionInterrupter {

        Monster monster = targetMonster.getMonster(user, target);

        if (!isHit(user, monster)) {
            return false;
        }
        monster.applyStatusCondition(condition);
        return true;
    }

    @Override
    public String getContext() {
        return "inflict status condition";
    }

    @Override
    public boolean needTarget() {
        return true;
    }
}