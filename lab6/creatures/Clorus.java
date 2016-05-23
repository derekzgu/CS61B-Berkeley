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
    private int r;
    /** green color. */
    private int g;
    /** blue color. */
    private int b;

    public Clorus(double e) {
        super("clorus");
    }

    public void attack(Creature c) {
    }

    public void move() {
        this.energy -= 0.15;
    }

    public void stay() {
        this.energy += 0.2;
    }

    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        return new Action(Action.ActionType.STAY);
    }

    public Color color() {
        g = 63;
        return color(r, g, b);
    }

    public Clorus replicate() {
        return this;
    }
}
