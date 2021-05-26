package unittests;

import action.*;
import ai.ntbea.BanditNTuple;
import model.Card;
import model.Position;

public class TestBanditNTuple
{
    public static void main(String[] args)
    {
        // TODO, enteder table positions
        int [] taplePositions = new int [] {0, 1};
        int    Kfactor        = 5;
        double epsilon        = 0.01;

        BanditNTuple bandit = new BanditNTuple(taplePositions, Kfactor, epsilon);

        // Add some Turns
        Action[] turn1  = new Action[5];
        Action[] turn2  = new Action[5];
        double   score = 6.0;

        Action a1 = new UnitAction    (new Position(0, 0), new Position(1, 2), UnitActionType.MOVE);
        Action a2 = new UnitAction    (new Position(0, 0), new Position(1, 2), UnitActionType.ATTACK);
        Action a3 = new DropAction    (Card.KNIGHT, new Position(1, 2));
        Action a4 = new DropAction    (Card.INFERNO, new Position(3, 3));
        Action a5 = new SwapCardAction(Card.ARCHER);

        // TODO, ¿hace lo que toca?
        turn1[0] = a1; turn1[1] = a2; turn1[2] = a3; turn1[3] = a4; turn1[4] = a5;
        turn2[0] = a2; turn2[1] = a1; turn2[2] = a3; turn2[3] = a4; turn2[4] = a5;

        bandit.add(turn1, score);
        System.out.println(bandit);
        System.out.println(bandit.ucb(turn1));
        System.out.println(bandit.ucb(turn2));

        bandit.add(turn2, score);
        System.out.println(bandit);
        System.out.println(bandit.ucb(turn1));
        System.out.println(bandit.ucb(turn2));
    }
}
