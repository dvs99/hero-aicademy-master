package unittests;

import action.*;
import ai.ntbea.BanditModel;
import model.Card;
import model.Position;

public class TestBanditModel
{
    public static void main(String[] args)
    {
        BanditModel bm1d = new BanditModel(5, 1.4, 0.01, false, false, false, false);
        BanditModel bm2d = new BanditModel(5, 1.4, 0.01, true, false, false, false);
        BanditModel bm3d = new BanditModel(5, 1.4, 0.01, true, true, false, false);
        BanditModel bm4d = new BanditModel(5, 1.4, 0.01, true, true, true, false);
        BanditModel bm2dcont = new BanditModel(5, 1.4, 0.01, true, false, false, true);

        Action a1 = new UnitAction(new Position(0, 0), new Position(1, 2), UnitActionType.MOVE);
        Action a2 = new UnitAction(new Position(0, 0), new Position(1, 2), UnitActionType.ATTACK);
        Action a3 = new DropAction(Card.KNIGHT, new Position(1, 2));
        Action a4 = new DropAction(Card.INFERNO, new Position(3, 3));
        Action a5 = new SwapCardAction(Card.ARCHER);

        // Add some Turns with some scores
        Action[] turn1 = new Action[5];
        Action[] turn2 = new Action[5];
        Action[] turn3 = new Action[5];
        Action[] turn4 = new Action[5];
        Action[] turn5 = new Action[5];
        double score1 = 6.0;
        double score2 = 4.0;

        turn1[0] = a1;        turn1[1] = a2;        turn1[2] = a3;        turn1[3] = a4;        turn1[4] = a5;
        turn2[0] = a2;        turn2[1] = a1;        turn2[2] = a3;        turn2[3] = a4;        turn2[4] = a5;
        turn3[0] = a4;        turn3[1] = a5;        turn3[2] = a1;        turn3[3] = a2;        turn3[4] = a3;
        turn4[0] = a1;        turn4[1] = a2;        turn4[2] = a5;        turn4[3] = a4;        turn4[4] = a3;
        turn5[0] = a2;        turn5[1] = a1;        turn5[2] = a5;        turn5[3] = a4;        turn5[4] = a3;

        bm1d.add(turn1, score1);
        bm2d.add(turn1, score1);
        bm3d.add(turn1, score1);
        bm4d.add(turn1, score1);
        bm2dcont.add(turn1, score1);

        System.out.println("Bandit Model 1D");
        System.out.println("   After adding ABCDE [6]");
        System.out.println("      ABCDE -> " + bm1d.ucb(turn1));
        System.out.println("      BACDE -> " + bm1d.ucb(turn2));
        System.out.println("      DEABC -> " + bm1d.ucb(turn3));
        System.out.println("      ABEDC -> " + bm1d.ucb(turn4));
        System.out.println("      BAEDC -> " + bm1d.ucb(turn5));

        System.out.println("Bandit Model 1D + 2D");
        System.out.println("   After adding ABCDE [6]");
        System.out.println("      ABCDE -> " + bm2d.ucb(turn1));
        System.out.println("      BACDE -> " + bm2d.ucb(turn2));
        System.out.println("      DEABC -> " + bm2d.ucb(turn3));
        System.out.println("      ABEDC -> " + bm2d.ucb(turn4));
        System.out.println("      BAEDC -> " + bm2d.ucb(turn5));

        System.out.println("Bandit Model 1D + 2D + 3D");
        System.out.println("   After adding ABCDE [6]");
        System.out.println("      ABCDE -> " + bm3d.ucb(turn1));
        System.out.println("      BACDE -> " + bm3d.ucb(turn2));
        System.out.println("      DEABC -> " + bm3d.ucb(turn3));
        System.out.println("      ABEDC -> " + bm3d.ucb(turn4));
        System.out.println("      BAEDC -> " + bm3d.ucb(turn5));

        System.out.println("Bandit Model 1D + 2D + 3D + 4D");
        System.out.println("   After adding ABCDE [6]");
        System.out.println("      ABCDE -> " + bm4d.ucb(turn1));
        System.out.println("      BACDE -> " + bm4d.ucb(turn2));
        System.out.println("      DEABC -> " + bm4d.ucb(turn3));
        System.out.println("      ABEDC -> " + bm4d.ucb(turn4));
        System.out.println("      BAEDC -> " + bm4d.ucb(turn5));

        System.out.println("Bandit Model 1D + 2D only contiguous");
        System.out.println("   After adding ABCDE [6]");
        System.out.println("      ABCDE -> " + bm2dcont.ucb(turn1));
        System.out.println("      BACDE -> " + bm2dcont.ucb(turn2));
        System.out.println("      DEABC -> " + bm2dcont.ucb(turn3));
        System.out.println("      ABEDC -> " + bm2dcont.ucb(turn4));
        System.out.println("      BAEDC -> " + bm2dcont.ucb(turn5));

        System.out.println("\n-------------\n");

        bm1d.add(turn2, score2);
        bm2d.add(turn2, score2);
        bm3d.add(turn2, score2);
        bm4d.add(turn2, score2);
        bm2dcont.add(turn2, score2);

        System.out.println("Bandit Model 1D");
        System.out.println("   After adding BACDE [4]");
        System.out.println("      ABCDE -> " + bm1d.ucb(turn1));
        System.out.println("      BACDE -> " + bm1d.ucb(turn2));
        System.out.println("      DEABC -> " + bm1d.ucb(turn3));
        System.out.println("      ABEDC -> " + bm1d.ucb(turn4));
        System.out.println("      BAEDC -> " + bm1d.ucb(turn5));

        System.out.println("Bandit Model 1D + 2D");
        System.out.println("   After adding BACDE [4]");
        System.out.println("      ABCDE -> " + bm2d.ucb(turn1));
        System.out.println("      BACDE -> " + bm2d.ucb(turn2));
        System.out.println("      DEABC -> " + bm2d.ucb(turn3));
        System.out.println("      ABEDC -> " + bm2d.ucb(turn4));
        System.out.println("      BAEDC -> " + bm2d.ucb(turn5));

        System.out.println("Bandit Model 1D + 2D + 3D");
        System.out.println("   After adding BACDE [4]");
        System.out.println("      ABCDE -> " + bm3d.ucb(turn1));
        System.out.println("      BACDE -> " + bm3d.ucb(turn2));
        System.out.println("      DEABC -> " + bm3d.ucb(turn3));
        System.out.println("      ABEDC -> " + bm3d.ucb(turn4));
        System.out.println("      BAEDC -> " + bm3d.ucb(turn5));

        System.out.println("Bandit Model 1D + 2D + 3D + 4D");
        System.out.println("   After adding BACDE [4]");
        System.out.println("      ABCDE -> " + bm4d.ucb(turn1));
        System.out.println("      BACDE -> " + bm4d.ucb(turn2));
        System.out.println("      DEABC -> " + bm4d.ucb(turn3));
        System.out.println("      ABEDC -> " + bm4d.ucb(turn4));
        System.out.println("      BAEDC -> " + bm4d.ucb(turn5));

        System.out.println("Bandit Model 1D + 2D only contiguous");
        System.out.println("   After adding BACDE [4]");
        System.out.println("      ABCDE -> " + bm2dcont.ucb(turn1));
        System.out.println("      BACDE -> " + bm2dcont.ucb(turn2));
        System.out.println("      DEABC -> " + bm2dcont.ucb(turn3));
        System.out.println("      ABEDC -> " + bm2dcont.ucb(turn4));
        System.out.println("      BAEDC -> " + bm2dcont.ucb(turn5));
    }
}
