package creatures;
import huglife.Creature;
import huglife.Direction;
import huglife.Action;
import huglife.Occupant;
import huglife.HugLifeUtils;
import java.awt.Color;
import java.util.Map;
import java.util.List;
/**
 * Created by chizhang on 5/23/16.
 */
public class Clorus extends Creature {

    /** red color. */
    private int r = 34;
    /** green color. */
    private int g = 0;
    /** blue color. */
    private int b = 231;

    public Clorus(double e) {
        super("clorus");
        energy = e;
    }

    public Clorus() {
        this(1);
    }

    public void attack(Creature c) {
        energy += c.energy();
    }

    public void move() {
        this.energy -= 0.03;
    }

    public void stay() {
        this.energy -= 0.01;
    }

    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        List<Direction> empties = getNeighborsOfType(neighbors, "empty");
        List<Direction> plips = getNeighborsOfType(neighbors, "plip");
        if (empties.size() == 0) {
            return new Action(Action.ActionType.STAY);
        } else if (plips.size() != 0) {
            Direction moveDir = HugLifeUtils.randomEntry(plips);
            return new Action(Action.ActionType.ATTACK, moveDir);
        } else {
            Direction moveDir = HugLifeUtils.randomEntry(empties);
            if (energy >= 1.0) {
                return new Action(Action.ActionType.REPLICATE, moveDir);
            } else {
                return new Action(Action.ActionType.MOVE, moveDir);
            }
        }
    }

    public Color color() {
        return color(r, g, b);
    }

    public Clorus replicate() {
        this.energy /= 2;
        return new Clorus(this.energy);
    }
}
