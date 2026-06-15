package edu.kit.kastel.actions;

import edu.kit.kastel.Element;
import edu.kit.kastel.monsters.Monster;
import edu.kit.kastel.reader.CompetitionInterrupter;

import java.util.List;
import java.util.ListIterator;
/**
 * The RepeatEffect class represents an effect that repeats a list of sub-effects
 * a specified number of times, as defined by a Count object.
 * @author uljyv
 * @version 1.0
 */
public final class RepeatEffect extends Effect {

    private List<Effect> effects;

    private RepeatEffect(Count count, List<Effect> effects) {
        setCount(count);
        this.effects = effects;
    }
    /**
     * Reads and constructs a RepeatEffect from a list iterator of input strings.
     * This method parses the effect definition and ensures correct formatting.
     *
     * @param lineIterator The iterator containing effect definitions.
     * @return A RepeatEffect instance if parsing is successful, otherwise null.
     */
    public static Effect readEffect(ListIterator<String> lineIterator) {

        if (!lineIterator.hasNext()) {
            return null;
        }

        String line = lineIterator.next().trim();
        String[] parts = line.split(" ");
        if (parts.length < 2) {
            return null;
        }

        Count count = Count.readCount(parts, 1, "repeat count");
        if (count == null) {
            return null;
        }

        List<Effect> effects  = readEffects(lineIterator);
        if (effects == null) {
            return null;
        }

        if (!lineIterator.hasNext()) {
            return null;
        }

        line = lineIterator.next().trim();

        if (!line.equals("end repeat")) {
            return null;
        }

        return new RepeatEffect(count, effects);
    }

    @Override
    public boolean applyEffect(Monster user, Monster target, Element actionElement, boolean isFirstInAction) throws CompetitionInterrupter {
        int repeatCount = getNextCountValue();

        boolean iternalIsFirstInAction = isFirstInAction;
        for (; repeatCount > 0; --repeatCount) {

            for (Effect effect : effects) {
                boolean resultEffect = effect.applyEffect(user, target, actionElement, isFirstInAction);
                if (iternalIsFirstInAction) {
                    if (!resultEffect) {
                        return false;
                    }
                    iternalIsFirstInAction = false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean needTarget() {
        for (Effect effect : effects) {
            if (effect.needTarget()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasNestedRepeat() {
        for (Effect effect : effects) {
            if (effect instanceof RepeatEffect) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getDamageStr() {
        return effects.get(0).getDamageStr();
    }

    @Override
    public String getHitRateStr() {
        return effects.get(0).getHitRateStr();
    }

    @Override
    protected Integer generateCountValue() throws CompetitionInterrupter {
        Integer repeatCount = super.generateCountValue();

        if (repeatCount == null) {
            return null;
        }

        for (int i = 0; i < repeatCount; i++) {
            for (Effect effect : effects) {
                effect.generateCountValue();
            }
        }
        return repeatCount;
    }
}