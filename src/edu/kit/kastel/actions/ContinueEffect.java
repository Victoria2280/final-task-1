package edu.kit.kastel.actions;

import edu.kit.kastel.Element;
import edu.kit.kastel.monsters.Monster;
import edu.kit.kastel.reader.CompetitionInterrupter;

import java.util.List;
import java.util.ListIterator;
/**
 * The ContinueEffect class represents an effect that applies multiple sub-effects
 * if a hit-rate condition is met. It extends EffectHasHitRate, meaning it has a
 * chance of applying based on probability.
 * @author uljyv
 * @version 1.0
 */
public final class ContinueEffect extends EffectHasHitRate {

    private List<Effect> effects;

    private ContinueEffect(int hitRate, List<Effect> effects) {
        super(hitRate);
        this.effects = effects;
    }

    @Override
    public String getContext() {
        return "continue";
    }
    /**
     * Reads and parses a ContinueEffect from the given iterator of strings.
     * The input should specify the hit rate and the sub-effects that will be executed.
     *
     * @param lineIterator A ListIterator of strings representing lines of effect data.
     * @return A ContinueEffect object if the input data is valid; otherwise null.
     */
    public static Effect readEffect(ListIterator<String> lineIterator) {

        if (!lineIterator.hasNext()) {
            return null;
        }

        String line = lineIterator.next().trim();

        String[] parts = line.split(" ");
        if (parts.length != 2) {
            return null;
        }

        int hitRate = 0;

        try {
            hitRate = Integer.parseInt(parts[1]);
        } catch (NumberFormatException nfe) {
            return null;
        }

        List<Effect> effects = Effect.readEffects(lineIterator);
        if (effects == null) {
            return null;
        }

        return new ContinueEffect(hitRate, effects);
    }

    @Override
    public boolean applyEffect(Monster user, Monster target, Element actionElement, boolean isFirstInAction) throws CompetitionInterrupter {
        if (!isHit(user, user)) {
            return false;
        }

        for (Effect effect : effects) {
            effect.applyEffect(user, target, actionElement, false);
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
    public String getDamageStr() {
        return effects.get(0).getDamageStr();
    }

    @Override
    protected Integer generateCountValue() throws CompetitionInterrupter {
        Integer repeatCount = super.generateCountValue();

        for (Effect effect : effects) {
            effect.generateCountValue();
        }

        return repeatCount;
    }
}