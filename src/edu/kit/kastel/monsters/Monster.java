package edu.kit.kastel.monsters;
import edu.kit.kastel.actions.ProtectTarget;
import edu.kit.kastel.reader.CompetitionInterrupter;
import edu.kit.kastel.reader.Main;
/**
 * The Monster class represents a monster in the game.
 * It encapsulates the monster's health, status conditions, protection status,
 * and various attributes such as attack, defense, speed, precision, and agility.
 * The class also provides methods for modifying these attributes, handling status
 * effects, and performing actions in the game.
 * @author uljyv
 * @version 1.0
 */
public class Monster {
    private int currentHP;
    private StatusCondition statusCondition = null;
    private ProtectTarget protectTarget = null;
    private int protectRounds = 0;
    private int spdLevel = 0;
    private int prcLevel = 0;
    private int defLevel = 0;
    private int atkLevel = 0;
    private int aglLevel = 0;
    private int prc = 1;
    private int agl = 1;
    private MonsterBase monsterBase = null;
    private int nameNumber = 0;
    /**
     * Constructs a Monster object based on the given MonsterBase template.
     * The monster starts with full HP as defined by the base stats.
     * @param monsterBase The base data for the monster, including its stats.
     */
    public Monster(MonsterBase monsterBase) {
        this.monsterBase = monsterBase;
        this.currentHP = monsterBase.getMaxHP();
    }
    /**
     * Returns the base stats of the monster.
     * @return The MonsterBase object containing base stats.
     */
    public MonsterBase getMonsterBase() {
        return monsterBase;
    }
    /**
     * Calculates the new level of a stat after applying a change.
     * Ensures the stat level stays within the range [-5, 5].
     * @param level  The current stat level.
     * @param change The amount to change the stat level by.
     * @return The adjusted stat level.
     */
    private int calcChangeLevel(int level, int change) {
        int result = level;

        result += change;
        if (result > 5) {
            return  5;
        }
        if (result < -5) {
            return -5;
        }
        return result;
    }
    /**
     * Prints the monster's current state, including HP bar, name, and status condition.
     * @param addData Additional text to display next to the monster's name.
     * @param number  The monster's unique number.
     */
    public void printMonster(String addData, int number) {
        String conditional;
        if (getCurrentHealth() == 0) {
            conditional = "(FAINTED)";
        } else if (statusCondition == null) {
            conditional = "(OK)";
        } else {
            switch (statusCondition) {
                case WET:
                    conditional = "(WET)";
                    break;
                case BURN:
                    conditional = "(BURN)";
                    break;
                case QUICKSAND:
                    conditional = "(QUICKSAND)";
                    break;
                case SLEEP:
                    conditional = "(SLEEP)";
                    break;
                default:
                    conditional = "(OK)";
                    break;
            }
        }
        String restHP = "X";
        String underscore = "_";
        System.out.println("[" + restHP.repeat((int) Math.ceil((double) (20 * getCurrentHealth()) / monsterBase.getMaxHP()))
                + underscore.repeat(20 - (int) Math.ceil((double) (20 * getCurrentHealth()) / monsterBase.getMaxHP()))
                + "] " + number + " " + addData + getName() + " " + conditional);

    }
    private String getStatsLevel(StatType type) {
        int level = getLevel(type);

        if (level == 0) {
            return "";
        }

        String sign = "";
        if (level > 0) {
            sign = "+";
        }
        return "(" + sign + level + ")";
    }
    /** Prints the monster's stats, including HP, attack, defense, speed, precision, and agility.*/
    public void printStats() {
        System.out.println("STATS OF " + getName());
        System.out.println("HP " + getCurrentHealth() + "/" + monsterBase.getMaxHP() + ", ATK " + monsterBase.getAtk()
            + getStatsLevel(StatType.ATK) + ", DEF " + monsterBase.getDef() + getStatsLevel(StatType.DEF) + ", SPD "
                + monsterBase.getSpd() + getStatsLevel(StatType.SPD) + ", PRC " + prc + getStatsLevel(StatType.PRC)
                + ", AGL " + agl + getStatsLevel(StatType.AGL));
    }
    /**
     * Changes the level of a specified stat.
     * @param type   The stat type to modify.
     * @param change The amount to change the stat by.
     */
    public void changeLevel(StatType type, int change) {
        int currentLevel = getLevel(type);
        int newLevel = calcChangeLevel(currentLevel, change);
        switch (type) {
            case AGL: aglLevel = newLevel;
                break;
            case ATK: atkLevel = newLevel;
                break;
            case DEF: defLevel = newLevel;
                break;
            case PRC: prcLevel = newLevel;
                break;
            case SPD: spdLevel = newLevel;
                break;
            default: break;
        }

        if (currentLevel != newLevel) {
            if (change < 0) {
                System.out.println(getName() + "'s " + type.statToString() + " decreases...");
            } else {
                System.out.println(getName() + "'s " + type.statToString() + " rises!");
            }
        }
    }

    private int getLevel(StatType type) {
        switch (type) {
            case AGL:
                return aglLevel;
            case ATK:
                return atkLevel;
            case DEF:
                return defLevel;
            case PRC:
                return prcLevel;
            case SPD:
                return spdLevel;
            default: return 0;
        }
    }

    private double calcStatFactor(StatType type) {
        int b = 3;
        if (type == StatType.ATK | type == StatType.DEF | type == StatType.SPD) {
            b = 2;
        }
        int level = getLevel(type);
        if (level >= 0) {
            return (double) (b + level) / b;
        }
        return (double) b / (b - level);
    }
    /**
     * Returns the monster's current defense value, considering its status condition.
     * @return The monster's current defense value.
     */
    public double getCurrentDef() {

        double rate = 1;
        if (statusCondition == StatusCondition.WET) {
            rate = 0.75;
        }
        return monsterBase.getDef() * rate * calcStatFactor(StatType.DEF);
    }
    /**
     * Returns the monster's current attack value, considering its status condition.
     * @return The monster's current attack value.
     */
    public double getCurrentAtk() {

        double rate = 1;
        if (statusCondition == StatusCondition.BURN) {
            rate = 0.75;
        }
        return monsterBase.getAtk() * rate * calcStatFactor(StatType.ATK);
    }

    /**
     * Returns the monster's current speed value, considering its status condition.
     * @return The monster's current speed value.
     */
    public double getCurrentSpd() {

        double rate = 1;
        if (statusCondition == StatusCondition.QUICKSAND) {
            rate = 0.75;
        }
        return monsterBase.getSpd() * rate * calcStatFactor(StatType.SPD);
    }
    /**
     * Returns the monster's current health (HP).
     * @return The monster's current HP.
     */
    public int getCurrentHealth() {
        return currentHP;
    }
    /**
     * Returns the current status condition of the monster.
     * @return The monster's current status condition.
     */
    public StatusCondition getStatusCondition() {
        return statusCondition;
    }
    /**
     * Returns the monster's current agility value.
     * @return The monster's current agility value.
     */
    public double getCurrentAgl() {
        return calcStatFactor(StatType.AGL);
    }
    /**
     * Returns the monster's current precision value.
     * @return The monster's current precision value.
     */
    public double getCurrentPrc() {
        return calcStatFactor(StatType.PRC);
    }
    /**
     * Applies a new status condition to the monster or removes an existing one.
     * @param newCondition The new StatusCondition to apply, or null to remove the current condition.
     */
    public void applyStatusCondition(StatusCondition newCondition) {
        if (this.statusCondition == newCondition) {
            return;
        }

        if (this.statusCondition == null) {
            this.statusCondition = newCondition;

            switch (statusCondition) {
                case WET -> System.out.println(getName() + " becomes soaking wet!");
                case BURN -> System.out.println(getName() + " caught on fire!");
                case SLEEP -> System.out.println(getName() + " falls asleep!");
                case QUICKSAND -> System.out.println(getName() + " gets caught by quicksand!");
                default -> {
                    return;
                }
            }
        } else if (newCondition == null) {
            switch (this.statusCondition) {
                case WET -> System.out.println(getName() + " dried up!");
                case BURN -> System.out.println(getName() + "'s burning has faded!");
                case SLEEP -> System.out.println(getName() + " woke up!");
                case QUICKSAND -> System.out.println(getName() + " escaped the quicksand!");
                default -> {
                    return;
                }
            }
            this.statusCondition = null;
        }
    }
    /**
     * Reduces the monster's HP by the specified damage amount.
     * @param damage The amount of damage to inflict.
     * @param tail Additional message from burning state.
     */
    public void takeDamage(int damage, String tail) {
        currentHP = Math.max(0, currentHP - damage);
        System.out.println(getName() + " takes " + damage + " damage" + tail + "!");
        if (currentHP == 0) {
            System.out.println(getName() + " faints!");
            return;
        }
    }
    /**
     * Heals the monster by the specified amount, ensuring it doesn't exceed its maximum HP.
     * @param heal The amount of health to restore.
     */
    public void heal(int heal) {
        currentHP = Math.min(monsterBase.getMaxHP(), currentHP + heal);
        System.out.println(getName() + " gains back " + heal + " health!");
    }
    /**
     * Applies protection to the monster, preventing damage for a specified number of rounds.
     * @param protectRounds The number of rounds the protection lasts.
     * @param protectTarget The type of protection (e.g., protecting health or stats).
     */
    public void applyProtection(int protectRounds, ProtectTarget protectTarget) {
        this.protectRounds = protectRounds;
        this.protectTarget = protectTarget;

        if (protectRounds > 0) {
            switch (protectTarget) {
                case STATS:
                    System.out.println(getName() + " is now protected against status changes!");
                    return;
                case HEALTH:
                    System.out.println(getName() + " is now protected against damage!");
                    return;
                default: return;
            }
        } else {
            System.out.println(getName() + "'s protection fades away...");
        }
    }
    /**
     * Returns the current protection target (e.g., health or stats).
     * @return The protection target.
     */
    public ProtectTarget getProtectTarget() {
        return protectTarget;
    }
    /**
     * Returns the name of the monster.
     * If the monster has a name number, it is appended to the name.
     * @return The monster's name.
     */
    public String getName() {
        if (nameNumber > 0) {
            return monsterBase.getName() + "#"  + nameNumber;
        }
        return monsterBase.getName();
    }
    /**
     * Sets the name number for the monster.
     * @param number The name number to set.
     */
    public void setNameNumber(int number) {
        nameNumber = number;
    }
    /**
     * Handles the monster's status condition at the start of an action.
     * If the monster has a status condition, there is a chance it will be removed.
     * @throws CompetitionInterrupter If the competition is interrupted.
     */
    public void startAction() throws CompetitionInterrupter {
        if (statusCondition == null) {
            return;
        }

        if (Main.random.decideProbability(1.0 / 3.0, "condition fades")) {
            applyStatusCondition(null);
        } else {
            switch (statusCondition) {
                case QUICKSAND:
                    System.out.println(getName() + " is caught in quicksand!");
                    return;
                case SLEEP:
                    System.out.println(getName() + " is asleep!");
                    return;
                case BURN:
                    System.out.println(getName() + " is burning!");
                    return;
                case WET:
                    System.out.println(getName() + " is soaking wet!");
                    return;
                default: return;
            }
        }
    }
    /**
     * Handles the monster's status condition at the end of an action.
     * Prints appropriate messages based on the current status condition.
     */
    public void endAction() {
        if (statusCondition == StatusCondition.BURN) {
            int damage = (int) Math.ceil(0.1 * monsterBase.getMaxHP());
            takeDamage(damage, " from burning");
        }
    }
    /**
     * Handles the monster's state at the end of a round.
     * Reduces protection duration and applies status effects such as burn damage.
     */
    public void endRound() {
        if (protectRounds > 0) {
            if (protectRounds == 1) {
                applyProtection(0, null);
            } else {
                --protectRounds;
            }
        }
    }
}