package ai.ntbea;

import action.*;
import model.Card;
import model.Position;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

//TODO: does this need to implement comparable?
//TODO: test speed
//TODO: prevent or test hash collisions? I think there are 8655 different actions possible. I don't know if it could be problem but I don't think so.
public class ActionArrayPattern /*implements Comparable<ActionArrayPattern> */{

    public static void main(String[] args) {
        //Unit tests

        Action[] a = {new UnitAction(new Position(1,2), new Position(1,4), UnitActionType.ATTACK), new DropAction(Card.ARCHER, new Position(1,2)), new SwapCardAction(Card.ARCHER), new SwapCardAction(Card.CRYSTAL), new SwapCardAction(Card.ARCHER)};
        Action[] b = {new UnitAction(new Position(1,2), new Position(1,4), UnitActionType.ATTACK), new DropAction(Card.ARCHER, new Position(1,2)), new SwapCardAction(Card.CRYSTAL), new SwapCardAction(Card.ARCHER), new SwapCardAction(Card.ARCHER)};
        int[] ix1 = {3, 4};
        int[] ix2 = {3, 4};
        int[] ix3 = {3, 4};
        ActionArrayPattern p1 = new ActionArrayPattern().setPattern(a , ix1);
        ActionArrayPattern p2 = new ActionArrayPattern().setPattern(b, ix2);
        ActionArrayPattern p3 = new ActionArrayPattern().setPattern(a, ix3);

        HashMap<ActionArrayPattern, Integer> test = new HashMap<>();
        test.put(p1, 1);
        test.put(p2, 2);
        test.put(p3, 3);
        System.out.println(test);
    }

    public Action[] v;

    public ActionArrayPattern setPattern(Action[] v) {
        this.v = v;
        return this;
    }

    public ActionArrayPattern setPattern(Action[] x, int[] ix) {
        v = new Action[ix.length];
        for (int i=0; i<ix.length; i++) {
            v[i] = x[ix[i]];
        }
        return this;
    }

    public ActionArrayPattern setPattern(List<Action> x, int[] ix) {
        v = new Action[ix.length];
        for (int i=0; i<ix.length; i++) {
            v[i] = x.get(ix[i]);
        }
        return this;
    }

    public int hashCode() {
        return Arrays.hashCode(v);
    }

    public  boolean equals(Object pattern) {
        try {
            ActionArrayPattern p = (ActionArrayPattern) pattern;
            for (int i = 0; i < v.length; i++) {
                if (!v[i].equals(p.v[i])) return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

/*
    public int compareTo(ActionArrayPattern p) {
        try {
            // now iterate over all the values
            for (int i=0; i<v.length; i++) {
                if (v[i] > p.v[i]) {
                    return 1;
                }
                if (v[i] < p.v[i]) {
                    return -1;
                }
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }
*/

    public String toString() {
        return Arrays.toString(v);
    }
}
