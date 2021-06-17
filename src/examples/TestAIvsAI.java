package examples;

import ai.AI;
import ai.GreedyActionAI;
import ai.RandomAI;
import ai.evaluation.HeuristicEvaluator;
import ai.ntbea.OnlineNTBEAGenomeBased;
import ai.util.RAND_METHOD;
import game.Game;
import game.GameArguments;
import model.DECK_SIZE;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

//TODO: print seed and allow it as input to replay games
public class TestAIvsAI
{

    public static void main(String[] args) throws IOException {
        runTest(false, new int[]{50, 100, 500, 1000, 3000, 6000, 10000},
                50,"testResults\\1DvsGREEDY.csv");
    }

    private static void runTest(boolean gfx, int[] budgets, int gamesOnEachSide, String path) throws IOException
    {
        PrintWriter writer = new PrintWriter(path, StandardCharsets.UTF_8);

        int p1Wins = 0;
        int p2Wins = 0;
        int firstP1Wins = 0;
        int firstP2Wins = 0;
        int lastSeed; //todo

        for (int budget: budgets){
            AI ai1 = new OnlineNTBEAGenomeBased(budget, 150, 12.5, 0.1, true, false, false, false, false, false, new HeuristicEvaluator(false));
            AI ai2 = new GreedyActionAI(new HeuristicEvaluator(false));
            for (int games = 0; games < gamesOnEachSide*2; games++)
            {
                AI aux = ai1;
                ai1 = ai2;
                ai2 = aux;

                GameArguments gameArgs = new GameArguments(gfx, ai1, ai2, "a", DECK_SIZE.STANDARD);
                gameArgs.budget = budget;
                Game game = new Game(null, gameArgs);
                game.run();

                String winnerString;
                if (game.state.getWinner() == 1)
                {
                    winnerString = ai1.title();
                    p1Wins++;
                    if (games % 2 == 0)
                        firstP1Wins++;
                    else
                        firstP2Wins++;
                }
                else if (game.state.getWinner() == 2)
                {
                    winnerString = ai2.title();
                    p2Wins++;
                    if (games % 2 == 0)
                        firstP2Wins++;
                    else
                        firstP1Wins++;
                }
                else winnerString = "None";

                System.out.println("Winner: Player " + game.state.getWinner() + ". P1 wins: " + p1Wins + ". P2 wins: " + p2Wins + ". " + game.player1.title() + " wins: " + ((games % 2 == 0)? firstP1Wins : firstP2Wins) + ". " + game.player2.title() + " wins: " + ((games % 2 == 0)? firstP2Wins : firstP1Wins) + ". Games: " + games + ".");

                String[] data = {winnerString, String.valueOf(game.state.getWinner()), String.valueOf(budget), String.valueOf(games)};
                String s = String.join(",", data);

                writer.println(s);
                writer.flush();
            }
        }
        writer.close();
    }
}
