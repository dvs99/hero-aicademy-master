package examples;

import ai.AI;
import ai.RandomAI;
import ai.evaluation.HeuristicEvaluator;
import ai.evaluation.RolloutEvaluator;
import ai.evolution.AiVisualizor;
import ai.evolution.OnlineEvolution;
import ai.mcts.Mcts;
import ai.ntbea.OnlineEAVisualizable;
import ai.ntbea.OnlineNTBEA;
import ai.ntbea.UniformMutator;
import ai.util.RAND_METHOD;
import model.DECK_SIZE;
import game.Game;
import game.GameArguments;

public class Examples {

	public static void main(String[] args) {
		
		//humanVsHuman();
		//humanVsAI(4000);
		AIVsAI(true, 1000);
	}

	private static void AIVsAI(boolean gfx, int budget) {
		while (true) {
			//Select p1 and p2
			AI p1= new OnlineEvolution(true, 100, 0.1, 0.5, budget, new HeuristicEvaluator(false));
			//AI p1 = new GreedyActionAI(new HeuristicEvaluator(false));
			//AI p1 = new Mcts(budget, new RolloutEvaluator(1, 1, new RandomAI(RAND_METHOD.TREE), new HeuristicEvaluator(false)));
			//AI p1 = new RandomAI(RAND_METHOD.BRUTE);
			AI p2 = new OnlineNTBEA(budget, 150, 66, 0.01f, true, true, false, true, new HeuristicEvaluator(false), new UniformMutator());

			GameArguments gameArgs = new GameArguments(gfx, p1, p2, "a", DECK_SIZE.STANDARD);
			gameArgs.budget = budget;
			Game game = new Game(null, gameArgs);

			//Enable visualizations if you want graphs and decide whether to plot the overall best or the current best/selected in each generation
			//((AiVisualizor) p2).enableVisualization(game.ui);
			//((OnlineEAVisualizable) p2).setPlotOverallBest(true);
			//((AiVisualizor) p1).enableVisualization(game.ui);
			//((OnlineEAVisualizable) p1).setPlotOverallBest(true);

			game.run();
			System.out.println("Winner: " + game.state.getWinner());
		}
	}

	private static void humanVsAI(int budget) {
		AI p1 = null;
		//AI p2 = new RandomAI(RAND_METHOD.BRUTE);
		//AI p2 = new GreedyActionAI(new HeuristicEvaluator(false));
		//AI p2 = new GreedyTurnAI(new HeuristicEvaluator(false), budget);
		AI p2 = new Mcts(budget, new RolloutEvaluator(1, 1, new RandomAI(RAND_METHOD.TREE), new HeuristicEvaluator(false)));
		//AI p2 = new OnlineIslandEvolution(true, 100, 0.1, 0.5, budget, new HeuristicEvaluator(false));
		
		GameArguments gameArgs = new GameArguments(true, p1, p2, "a", DECK_SIZE.STANDARD);
		gameArgs.budget = budget; 
		Game game = new Game(null, gameArgs);
		
		game.run();
	}

	private static void humanVsHuman() {
		AI p1 = null;
		AI p2 = null;
		
		Game game = new Game(null, new GameArguments(true, p1, p2, "a", DECK_SIZE.STANDARD));
		game.run();
	}
}