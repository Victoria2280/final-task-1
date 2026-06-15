package edu.kit.kastel.actions;

import edu.kit.kastel.Element;
import edu.kit.kastel.monsters.Monster;
import edu.kit.kastel.reader.CompetitionInterrupter;

import java.util.ListIterator;
/**
 * The ProtectStat class represents an effect that grants protection to a monster for a specified number of turns.
 * This effect has a hit rate, meaning it may fail to apply under certain conditions.
 * It extends EffectHasHitRate.
 * @author uljyv
 * @version 1.0
 */
public final class ProtectStat extends EffectHasHitRate {

    private ProtectTarget type;

    private ProtectStat(Count count, ProtectTarget type, int hitRate) {
        super(hitRate);
        setCount(count);
        this.type = type;
    }
    /**
     * Reads and parses a ProtectStat effect from a given list iterator.
     *
     * @param lineIterator The iterator containing effect details.
     * @return A new ProtectStat instance if parsing succeeds, otherwise null.
     */
    public static Effect readEffect(ListIterator<String> lineIterator)  {
        if (!lineIterator.hasNext()) {
            return null;
        }

        String line = lineIterator.next().trim();
        String[] parts = line.split(" ");

        if (parts.length < 3) {
            return null;
        }

        ProtectTarget protectTarget = ProtectTarget.getProtectTarget(parts[1]);
        if (protectTarget == null) {
            return null;
        }

        Count count = Count.readCount(parts, 2, "protect count");
        if (count == null) {
            return null;
        }

        int hitRateIndex = 3;
        if (count.getType() == CountType.RANDOM) {
            hitRateIndex = 5;
        }

        try {
            int hitRate = Integer.parseInt(parts[hitRateIndex]);
            return new ProtectStat(count, protectTarget, hitRate);
        } catch (NumberFormatException nfe) {
            return null;
        }
    }

    @Override
    public  boolean applyEffect(Monster user, Monster target, Element actionElement, boolean isFirstInAction)
            throws CompetitionInterrupter {

        if (!isHit(user, user)) {
            return false;
        }

        user.applyProtection(getNextCountValue() + 1, type);

        return true;
    }

    @Override
    public String getContext() {
        return "protection count";
    }
}