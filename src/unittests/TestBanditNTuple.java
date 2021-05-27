package unittests;

import action.*;
import ai.ntbea.BanditNTuple;
import model.Card;
import model.Position;

public class TestBanditNTuple
{
    public static void main(String[] args)
    {
        double Kfactor        = 1.4;
        double epsilon        = 0.01;

        // One bandit for each dimesion
        BanditNTuple bandit1 = new BanditNTuple(new int [] {0}, Kfactor, epsilon);
        BanditNTuple bandit2 = new BanditNTuple(new int [] {1}, Kfactor, epsilon);
        BanditNTuple bandit3 = new BanditNTuple(new int [] {2}, Kfactor, epsilon);
        BanditNTuple bandit4 = new BanditNTuple(new int [] {3}, Kfactor, epsilon);
        BanditNTuple bandit5 = new BanditNTuple(new int [] {4}, Kfactor, epsilon);

        // Some actions
        Action a1 = new UnitAction    (new Position(0, 0), new Position(1, 2), UnitActionType.MOVE);
        Action a2 = new UnitAction    (new Position(0, 0), new Position(1, 2), UnitActionType.ATTACK);
        Action a3 = new DropAction    (Card.KNIGHT, new Position(1, 2));
        Action a4 = new DropAction    (Card.INFERNO, new Position(3, 3));
        Action a5 = new SwapCardAction(Card.ARCHER);

        // Add some Turns with some scores
        Action[] turn1  = new Action[5];
        Action[] turn2  = new Action[5];
        Action[] turn3  = new Action[5];
        double   score1 = 6.0;
        double   score2 = 4.0;

        turn1[0] = a1; turn1[1] = a2; turn1[2] = a3; turn1[3] = a4; turn1[4] = a5;
        turn2[0] = a2; turn2[1] = a1; turn2[2] = a3; turn2[3] = a4; turn2[4] = a5;
        turn3[0] = a4; turn3[1] = a5; turn3[2] = a1; turn3[3] = a2; turn3[4] = a3;

        bandit1.add(turn1, score1);
        bandit2.add(turn1, score1);
        bandit3.add(turn1, score1);
        bandit4.add(turn1, score1);
        bandit5.add(turn1, score1);

        System.out.println(bandit1);
        System.out.println(bandit1.ucb(turn1));
        System.out.println(bandit1.ucb(turn2));
        System.out.println(bandit1.ucb(turn3));

        System.out.println(bandit2);
        System.out.println(bandit2.ucb(turn1));
        System.out.println(bandit2.ucb(turn2));
        System.out.println(bandit2.ucb(turn3));

        System.out.println(bandit3);
        System.out.println(bandit3.ucb(turn1));
        System.out.println(bandit3.ucb(turn2));
        System.out.println(bandit3.ucb(turn3));

        System.out.println(bandit4);
        System.out.println(bandit4.ucb(turn1));
        System.out.println(bandit4.ucb(turn2));
        System.out.println(bandit4.ucb(turn3));

        System.out.println(bandit5);
        System.out.println(bandit5.ucb(turn1));
        System.out.println(bandit5.ucb(turn2));
        System.out.println(bandit5.ucb(turn3));

        System.out.println("\n-------------\n");
        bandit1.add(turn2, score2);
        bandit2.add(turn2, score2);
        bandit3.add(turn2, score2);
        bandit4.add(turn2, score2);
        bandit5.add(turn2, score2);

        System.out.println(bandit1);
        System.out.println(bandit1.ucb(turn1));
        System.out.println(bandit1.ucb(turn2));
        System.out.println(bandit1.ucb(turn3));

        System.out.println(bandit2);
        System.out.println(bandit2.ucb(turn1));
        System.out.println(bandit2.ucb(turn2));
        System.out.println(bandit2.ucb(turn3));

        System.out.println(bandit3);
        System.out.println(bandit3.ucb(turn1));
        System.out.println(bandit3.ucb(turn2));
        System.out.println(bandit3.ucb(turn3));

        System.out.println(bandit4);
        System.out.println(bandit4.ucb(turn1));
        System.out.println(bandit4.ucb(turn2));
        System.out.println(bandit4.ucb(turn3));

        System.out.println(bandit5);
        System.out.println(bandit5.ucb(turn1));
        System.out.println(bandit5.ucb(turn2));
        System.out.println(bandit5.ucb(turn3));


    }
}
