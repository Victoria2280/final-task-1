package edu.kit.kastel.actions;

import edu.kit.kastel.Element;
import edu.kit.kastel.monsters.Monster;
import edu.kit.kastel.reader.CompetitionInterrupter;
import edu.kit.kastel.reader.Main;
/**
 * The DamageHealCommonEffect class serves as a base class for effects that
 * involve dealing damage or healing a target. It extends EffectHasHitRate
 * and includes mechanics such as critical hits, elemental effectiveness, and
 * random damage variation.
 * This class is used as a parent for both damage-dealing and healing effects,
 * applying various game mechanics such as type effectiveness, critical hits, and randomness.
 * @author uljyv
 * @version 1.0
 */
public abstract class DamageHealCommonEffect extends EffectHasHitRate {
    static final double MAX_RANDOM_FACTOR = 1;
    static final double MIN_RANDOM_FACTOR = 0.85;
    static final double MAX_SAME_ELEMENT_FACTOR = 1.5;
    static final double EFFECTIVE_ELEMENT_FACTOR = 2;
    static final double NOT_EFFECTIVE_ELEMENT_FACTOR = 0.5;
    protected Strength strength;
    private double sameElementFactor = 1;
    private int criticalFactor = 1;
    /**
     * Constructs a DamageHealCommonEffect with the specified strength and hit rate.
     *
     * @param strength The strength of the effect, which determines the damage or healing amount.
     * @param hitRate  The probability (as a percentage) that the effect will be applied successfully.
     */
    public DamageHealCommonEffect(Strength strength, int hitRate) {
        super(hitRate);
        this.strength = strength;
    }

    private double getRandomFactor() throws CompetitionInterrupter {
        return Main.random.getBetween(MIN_RANDOM_FACTOR, MAX_RANDOM_FACTOR, "random factor");
    }

    private double getSameElementFactor(Element actionElement, Monster user) {
        if (actionElement == user.getMonsterBase().getElement()) {
            sameElementFactor = MAX_SAME_ELEMENT_FACTOR;
        }
        return sameElementFactor;
    }

    private int getCriticalHit(Monster user, Monster target) throws CompetitionInterrupter {
        double critProb = Math.pow(10, -target.getCurrentSpd() / user.getCurrentSpd());
        if (Main.random.decideProbability(critProb, getContext())) {
            System.out.println("Critical hit!");
            return 2;
        }
        return criticalFactor;
    }

    private double getElementFactor(Element actionElement, Monster target) {
        Element targetElement = target.getMonsterBase().getElement();

        if (isElementVeryEffective(actionElement, targetElement)) {
            if (!Action.wasEffectivenessMessage) {
                System.out.println("It is very effective!");
                Action.wasEffectivenessMessage = true;
            }
            return EFFECTIVE_ELEMENT_FACTOR;
        } else {

            if (actionElement == targetElement || actionElement == Element.NORMAL || targetElement == Element.NORMAL) {
                return 1;
            } else {
                if (!Action.wasEffectivenessMessage) {
                    System.out.println("It is not very effective...");
                    Action.wasEffectivenessMessage = true;
                }
                return NOT_EFFECTIVE_ELEMENT_FACTOR;
            }
        }
    }

    private boolean isElementVeryEffective(Element userActionElement, Element targetElement) {
        return (userActionElement == Element.WATER & targetElement == Element.FIRE)
                || (userActionElement == Element.FIRE & targetElement == Element.EARTH)
                || (userActionElement == Element.EARTH & targetElement == Element.WATER);
    }
    /**
     * Calculates the base damage value of an attack considering various factors
     * such as attack-defense ratio, critical hits, element multipliers, and randomness.
     *
     * @param user          The monster using the attack.
     * @param target        The target monster.
     * @param actionElement The element of the action.
     * @return The computed damage value.
     * @throws CompetitionInterrupter If the competition is interrupted.
     */
    public int getBaseDamage(Monster user, Monster target, Element actionElement) throws CompetitionInterrupter {

        double damage = strength.getValue();
        damage *= getElementFactor(actionElement, target);
        damage *= (user.getCurrentAtk() / target.getCurrentDef());
        damage *= getCriticalHit(user, target);
        damage *= getSameElementFactor(actionElement, user);
        damage *= getRandomFactor() / 3;

        return (int) Math.ceil(damage);
    }
    /**
     * Returns the absolute damage value without any additional calculations.
     *
     * @return The raw strength value.
     */
    public int getAbs() {
        return strength.getValue();
    }
}