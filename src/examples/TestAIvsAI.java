package examples;

import ai.AI;
import ai.evaluation.HeuristicEvaluator;
import ai.evolution.OnlineEvolution;
import ai.ntbea.OnlineNTBEAGenomeBased;
import ai.ntbea.UniformMutator;
import game.Game;
import game.GameArguments;
import model.DECK_SIZE;

//TODO: print seed and allow it as input to replay games
public class TestAIvsAI
{

    public static void main(String[] args)
    {
        runTest(true, 6000,
                new OnlineEvolution(true, 100, 0.1, 0.5, 6000, new HeuristicEvaluator(false)),
                new OnlineNTBEAGenomeBased(6000, 150, 1.45, 0.01f, false, true, false, false, false, true, new HeuristicEvaluator(false)),
                2);
    }

    private static void runTest(boolean gfx, int budget, AI ai1, AI ai2, int gamesOnEachSide)
    {
        int p1Wins = 0;
        int p2Wins = 0;
        int firstP1Wins = 0;
        int firstP2Wins = 0;
        int lastSeed; //todo

        for (int games = 1; games <= gamesOnEachSide*2; games++)
        {
            AI aux = ai1;
            ai1 = ai2;
            ai2 = aux;

            GameArguments gameArgs = new GameArguments(gfx, ai1, ai2, "a", DECK_SIZE.STANDARD);
            gameArgs.budget = budget;
            Game game = new Game(null, gameArgs);
            game.run();

            if (game.state.getWinner() == 1)
            {
                p1Wins++;
                if (games % 2 == 0)
                    firstP1Wins++;
                else
                    firstP2Wins++;
            }
            else if (game.state.getWinner() == 2)
            {
                p2Wins++;
                if (games % 2 == 0)
                    firstP2Wins++;
                else
                    firstP1Wins++;
            }
            System.out.println("Winner: Player " + game.state.getWinner() + ". P1 wins: " + p1Wins + ". P2 wins: " + p2Wins + ". " + game.player1.title() + " wins: " + ((games % 2 == 0)? firstP1Wins : firstP2Wins) + ". " + game.player2.title() + " wins: " + ((games % 2 == 0)? firstP2Wins : firstP1Wins) + ". Games: " + games + ".");
        }
    }
}
