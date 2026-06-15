package edu.kit.kastel.actions;

import edu.kit.kastel.Element;
import edu.kit.kastel.monsters.Monster;
import edu.kit.kastel.monsters.StatType;
import edu.kit.kastel.monsters.TargetMonster;
import edu.kit.kastel.reader.CompetitionInterrupter;

import java.util.ListIterator;
/**
 * The InflictStatChange class represents an effect that modifies a monster's stats.
 * It extends EffectHasHitRate and applies a stat change to a target monster if the effect hits.
 * @author uljyv
 * @version 1.0
 */
public class InflictStatChange extends EffectHasHitRate {

    private int change;
    private StatType type;
    private TargetMonster targetMonster;
    /**
     * Constructs an InflictStatChange effect with the specified parameters.
     *
     * @param hitRate The probability (in percentage) that the effect will successfully apply.
     * @param change The magnitude of the stat change.
     * @param type The type of stat being modified.
     * @param targetMonster The target monster affected by the stat change.
     */
    public InflictStatChange(int hitRate, int change, StatType type, TargetMonster targetMonster) {
        super(hitRate);
        this.change = change;
        this.type = type;
        this.targetMonster = targetMonster;
    }
    /**
     * Reads an InflictStatChange effect from a given list iterator.
     * Parses the effect parameters and returns a new instance.
     *
     * @param lineIterator The iterator containing effect details.
     * @return A new InflictStatChange instance if parsing succeeds, otherwise null.
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

        StatType statType = StatType.getStatType(parts[2]);
        if (statType == null) {
            return null;
        }

        try {
            int change = Integer.parseInt(parts[3]);
            int hitRate = Integer.parseInt(parts[4]);
            return new InflictStatChange(hitRate, change, statType, target);
        } catch (NumberFormatException nfe) {
            return null;
        }
    }

    @Override
    public boolean applyEffect(Monster user, Monster target, Element actionElement, boolean isFirstInAction) throws CompetitionInterrupter {

        Monster monster = targetMonster.getMonster(user, target);

        if (targetMonster != TargetMonster.USER & change < 0) {
            if (monster.getProtectTarget() == ProtectTarget.STATS) {
                System.out.println(monster.getName() + " is protected and is unaffected!");
                return false;
            }
        }

        if (!isHit(user, monster)) {
            return false;
        }
        monster.changeLevel(type, change);
        return true;
    }

    @Override
    public String getContext() {
        return "inflict stat change";
    }

    @Override
    public boolean needTarget() {

        return targetMonster == TargetMonster.TARGET;
    }
}