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
import java.util.List;

public class Tournament
{
    List<String>  l_bots_main;
    List<String>  l_bots_sub;
    List<Integer> l_budgets;
    int           n_games;
    String        output_filename;

    public Tournament(List<String> l_bots_main, List<String>  l_bots_sub, List<Integer> l_budgets, int n_games, String output_filename)
    {
        this.l_bots_main     = l_bots_main;
        this.l_bots_sub      = l_bots_sub;
        this.l_budgets       = l_budgets;
        this.n_games         = n_games;
        this.output_filename = output_filename;
    }

    public void Play() throws IOException
    {
        PrintWriter writer = new PrintWriter(output_filename, StandardCharsets.UTF_8);

        for (String bot_main: l_bots_main)
            for (String bot_sub : l_bots_sub)
            {
                boolean early_abort_budget = false;
                int bot_main_wins = 0;
                int bot_sub_wins = 0;
                for (Integer budget : l_budgets)
                {
                    if (!early_abort_budget)
                    {
                        for (int igame = 0; igame < n_games; igame++)
                        {
                            int winner = PlayGame(bot_main, bot_sub, budget, writer);
                            if (winner == 1)
                                bot_main_wins++;
                            else if (winner == 2)
                                bot_sub_wins++;
                        }
                        for (int igame = 0; igame < n_games; igame++)
                        {
                            int winner = PlayGame(bot_sub, bot_main, budget, writer);
                            if (winner == 1)
                                bot_sub_wins++;
                            else if (winner == 2)
                                bot_main_wins++;
                        }
                        if (bot_main_wins == n_games * 2 || bot_sub_wins == n_games * 2)
                            early_abort_budget = true;
                    }
                }
            }
        writer.close();
    }

    private int PlayGame(String bot1_name, String bot2_name, int budget, PrintWriter writer)
    {
        AI bot1;
        AI bot2;

        bot1 = GetBot(bot1_name, budget);
        bot2 = GetBot(bot2_name, budget);

        GameArguments gameArgs = new GameArguments(false, bot1, bot2, "a", DECK_SIZE.STANDARD);
        gameArgs.gfx    = false;
        gameArgs.budget = budget;

        Game game = new Game(null, gameArgs);
        game.run();
        int winner = game.state.getWinner();
        System.out.println(bot1.title() + "," + bot2.title() + "," + budget + "," + winner);

        String[] data = {bot1.title(), bot2.title(), String.valueOf(budget), String.valueOf(game.state.getWinner()) };
        String s = String.join(",", data);

        writer.println(s);
        writer.flush();
        return  winner;
    }

    private AI GetBot(String name, int budget)
    {
        return switch (name)
                {
                    case "Random"    -> new RandomAI(RAND_METHOD.BRUTE);
                    case "Greedy"    -> new GreedyActionAI(new HeuristicEvaluator(false));
                    case "NTBEA1D"   -> new OnlineNTBEAGenomeBased(budget, 150, 12.5, 0.1, true, false, false, false, false, false, new HeuristicEvaluator(false));
                    case "NTBEA1D2D" -> new OnlineNTBEAGenomeBased(budget, 150, 12.5, 0.1, true, true, false, false, false, false, new HeuristicEvaluator(false));
                    default          -> new RandomAI(RAND_METHOD.BRUTE);
                };
    }


}
