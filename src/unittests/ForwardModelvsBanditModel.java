package unittests;

import action.*;
import ai.evaluation.HeuristicEvaluator;
import ai.ntbea.BanditModel;
import game.Game;
import game.GameState;
import model.Card;
import model.HaMap;
import model.Position;
import util.MapLoader;

import java.io.IOException;

public class ForwardModelvsBanditModel
{
    public static void main(String[] args)
    {

        double n_times = 1e6;

        BanditModel bm1d = new BanditModel(5, 1.4, 0.01, true,false, false, false, false, true);
        BanditModel bm2d = new BanditModel(5, 1.4, 0.01, true,true, false, false, false, true);
        BanditModel bm3d = new BanditModel(5, 1.4, 0.01, true,true, true, false, false, true);
        BanditModel bm4d = new BanditModel(5, 1.4, 0.01, true,true, true, true, false, true);
        BanditModel bm2dcont = new BanditModel(5, 1.4, 0.01, true, true, false, false, true, true);

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
        double score3 = 3.0;
        double score4 = 7.0;
        double score5 = 1.0;

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

        bm1d.add(turn2, score2);
        bm2d.add(turn2, score2);
        bm3d.add(turn2, score2);
        bm4d.add(turn2, score2);
        bm2dcont.add(turn2, score2);

        bm1d.add(turn3, score3);
        bm2d.add(turn3, score3);
        bm3d.add(turn3, score3);
        bm4d.add(turn3, score3);
        bm2dcont.add(turn3, score3);

        bm1d.add(turn4, score4);
        bm2d.add(turn4, score4);
        bm3d.add(turn4, score4);
        bm4d.add(turn4, score4);
        bm2dcont.add(turn4, score4);

        bm1d.add(turn5, score5);
        bm2d.add(turn5, score5);
        bm3d.add(turn5, score5);
        bm4d.add(turn5, score5);
        bm2dcont.add(turn5, score5);

        // -----------------------------

        long start = System.currentTimeMillis();
        for (int i=0; i<n_times; i++)
        {
            bm1d.ucb(turn1);
            bm1d.ucb(turn2);
            bm1d.ucb(turn3);
            bm1d.ucb(turn4);
            bm1d.ucb(turn5);
        }
        long time_bm = System.currentTimeMillis() - start;
        System.out.println("Time BanditModel 1D: " + time_bm);

        start = System.currentTimeMillis();
        for (int i=0; i<n_times; i++)
        {
            bm2dcont.ucb(turn1);
            bm2dcont.ucb(turn2);
            bm2dcont.ucb(turn3);
            bm2dcont.ucb(turn4);
            bm2dcont.ucb(turn5);
        }
        time_bm = System.currentTimeMillis() - start;
        System.out.println("Time BanditModel 2D cont: " + time_bm);


        start = System.currentTimeMillis();
        for (int i=0; i<n_times; i++)
        {
            bm2d.ucb(turn1);
            bm2d.ucb(turn2);
            bm2d.ucb(turn3);
            bm2d.ucb(turn4);
            bm2d.ucb(turn5);
        }
        time_bm = System.currentTimeMillis() - start;
        System.out.println("Time BanditModel 2D: " + time_bm);

        start = System.currentTimeMillis();
        for (int i=0; i<n_times; i++)
        {
            bm3d.ucb(turn1);
            bm3d.ucb(turn2);
            bm3d.ucb(turn3);
            bm3d.ucb(turn4);
            bm3d.ucb(turn5);
        }
        time_bm = System.currentTimeMillis() - start;
        System.out.println("Time BanditModel 3D: " + time_bm);

        start = System.currentTimeMillis();
        for (int i=0; i<n_times; i++)
        {
            bm4d.ucb(turn1);
            bm4d.ucb(turn2);
            bm4d.ucb(turn3);
            bm4d.ucb(turn4);
            bm4d.ucb(turn5);
        }
        time_bm = System.currentTimeMillis() - start;
        System.out.println("Time BanditModel 4D: " + time_bm);



        // ----------------------------
        // Forward Model
        HeuristicEvaluator evaluator = new HeuristicEvaluator(true);
        HaMap map;

        try{
            map = MapLoader.get("a");
        } catch (IOException e){
            System.out.println("Map not found.");
            return;
        }

        GameState state = new GameState(map);
        GameState next1 = new GameState(map);
        GameState next2 = new GameState(map);
        GameState next3 = new GameState(map);
        GameState next4 = new GameState(map);
        GameState next5 = new GameState(map);

        start = System.currentTimeMillis();
        for (int i=0; i<n_times; i++)
        {
            next1.imitate(state);
            for (Action a : turn1)
                next1.update(a);
            evaluator.eval(next1, state.p1Turn);

            next2.imitate(state);
            for (Action a : turn2)
                next2.update(a);
            evaluator.eval(next2, state.p1Turn);

            next3.imitate(state);
            for (Action a : turn2)
                next2.update(a);
            evaluator.eval(next2, state.p1Turn);

            next4.imitate(state);
            for (Action a : turn4)
                next4.update(a);
            evaluator.eval(next4, state.p1Turn);

            next5.imitate(state);
            for (Action a : turn5)
                next5.update(a);
            evaluator.eval(next5, state.p1Turn);
        }
        time_bm = System.currentTimeMillis() - start;
        System.out.println("Time ForwardModel: " + time_bm);
    }
}
