package examples;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Experiment
{
    public static void main(String[] args) throws IOException
    {
        List<String>  l_bots_main = new ArrayList<>();
        List<String>  l_bots_sub  = new ArrayList<>();

        List<Integer> l_budgets = new ArrayList<>();
        int           n_games;

        l_bots_main.add("NTBEA1D");
        l_bots_main.add("NTBEA1D2D");

        l_bots_sub.add("Random");
        l_bots_sub.add("Greedy");

        l_budgets.add(50);
        l_budgets.add(100);
        l_budgets.add(500);
        l_budgets.add(1000);
        l_budgets.add(3000);
        l_budgets.add(6000);
        l_budgets.add(10000);

        n_games = 10;

        String output_file = "testResults//exp.txt";

        Tournament t = new Tournament(l_bots_main, l_bots_sub, l_budgets, n_games, output_file);
        t.Play();
    }
}
