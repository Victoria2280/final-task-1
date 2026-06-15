package edu.kit.kastel.actions;

import edu.kit.kastel.monsters.Monster;
import edu.kit.kastel.monsters.StatusCondition;
import edu.kit.kastel.reader.CompetitionInterrupter;
import edu.kit.kastel.reader.Main;
/**
 * The EffectHasHitRate class is an abstract extension of Effect that
 * introduces a hit rate mechanic. This determines whether an effect successfully
 * applies based on probability calculations influenced by the user's and target's stats.
 * @author uljyv
 * @version 1.0
 */
public abstract class EffectHasHitRate extends Effect {

    private int hitRate;
    /**
     * Constructs an EffectHasHitRate instance with the specified hit rate.
     *
     * @param hitRate The base probability (in percentage) of the effect hitting.
     */
    public EffectHasHitRate(int hitRate) {
        this.hitRate = hitRate;
    }
    /**
     * Provides context for probability calculations, typically used for logging
     * or debugging hit/miss outcomes.
     *
     * @return A string representing the effect's context.
     */
    public abstract String getContext();
    /**
     * Determines whether the effect successfully hits the target based on the
     * user's and target's stats, as well as the effect's base hit rate.
     *
     * @param user   The monster using the effect.
     * @param target The target monster.
     * @return true if the effect successfully hits, false otherwise.
     * @throws CompetitionInterrupter If the competition is interrupted.
     */
    public boolean isHit(Monster user, Monster target) throws CompetitionInterrupter {
        if (user.getStatusCondition() != StatusCondition.SLEEP && target.getCurrentHealth() != 0) {
            double probHit = 0;
            if (!user.getName().equals(target.getName())) {
                probHit = ((double) hitRate / 100) * (user.getCurrentPrc() / target.getCurrentAgl());

            } else {
                probHit = ((double) hitRate / 100) * user.getCurrentPrc();
            }
            return Main.random.decideProbability(probHit, getContext());
        }
        return false;
    }

    @Override
    public String getHitRateStr() {
        return String.valueOf(hitRate);
    }
}