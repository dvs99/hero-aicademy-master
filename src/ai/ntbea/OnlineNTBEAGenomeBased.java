package ai.ntbea;

import action.Action;
import action.EndTurnAction;
import action.SingletonAction;
import ai.AI;
import ai.evaluation.IStateEvaluator;
import ai.evolution.AiVisualizor;
import ai.evolution.Genome;
import ai.evolution.OnlineEvolutionVisualizor;
import ai.evolution.WeakGenome;
import ai.util.ActionPruner;
import game.GameState;
import ui.UI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Implements the NTBEA algorithm to evolve a turn and play it
//Note that it will only work properly as p2 for now
//Note: plotting the results may worsen performance, not tested yet
//TODO: Make it work on 3-dimensional turns so it works properly on the first turn if its p1
//TODO: try starting with online evolution
//TODO: test what kFactors and eValues are adequate to properly balance exploration & exploitation
//TODO: it seems like we are playing invalid actions sometimes, check why it's happening? not sure if its happening withe genome based approach
//TODO: prevent crash when the genome actions list is shorter then the needed length
public class OnlineNTBEAGenomeBased implements AI, AiVisualizor, OnlineEAVisualizable {
    private  BanditModel model = null;
    private  final ActionPruner pruner;
    private final IStateEvaluator evaluator;
    private final Mutator mutator;

    private ArrayList<Action> turn; //todo: maybe change to array or non mutable list for efficiency
    private int i;

    private final int budget;
    private final int nNeighbours;
    private final Action[][] neighbourBuffer;

    private final double kFactor;
    private final double eValue;

    private final boolean use2D;
    private final boolean use3D;
    private final boolean use4D;
    private final boolean useOnlyContiguous;

    private OnlineEvolutionVisualizor visualizor;
    private final Map<Integer, Double> fitnesses;
    private final List<List<Action>> bestActions;
    private Boolean plotOverallBest = true;

    public OnlineNTBEAGenomeBased(int budget, int nNeighbours, double kFactor, double eValue, boolean use2D, boolean use3D, boolean use4D, boolean useOnlyContiguous, IStateEvaluator evaluator, Mutator mutator){
        turn = new ArrayList<>();
        pruner = new ActionPruner();
        neighbourBuffer = new Action[nNeighbours][5];
        this.mutator = mutator;
        this.evaluator = evaluator;
        this.budget = budget;
        this.nNeighbours = nNeighbours;
        this.kFactor = kFactor;
        this.eValue = eValue;
        this.use2D = use2D;
        this.use3D = use3D;
        this.use4D = use4D;
        this.useOnlyContiguous = useOnlyContiguous;
        this.fitnesses = new HashMap<>();
        this.bestActions = new ArrayList<>();
    }

    @Override
    public Map<Integer, Double> getFitnesses() {
        return fitnesses;
    }

    @Override
    public List<List<Action>> getBestActions() {
        return bestActions;
    }

    @Override
    public void setPlotOverallBest(boolean value) {
        plotOverallBest = value;
    }

    @Override
    public void enableVisualization(UI ui){
        this.visualizor = new OnlineEvolutionVisualizor(ui, this);
    }

    @Override
    public Action act(GameState state, long ms) {
        //TODO fix turn 1
        if (state.turn ==1)
            return new EndTurnAction();

        if (turn.isEmpty())
            runEA(state);

        final Action next = turn.get(0);
        turn.remove(0);
        return next;
    }


    private void runEA(GameState state) {
        if (visualizor != null){
            fitnesses.clear();
            bestActions.clear();
        }

        long start = System.currentTimeMillis();
        GameState stateCopy = new GameState(state.map);

        //create or reset the model as needed
        if (model == null)
            model = new BanditModel(5, kFactor, eValue, use2D, use3D, use4D, useOnlyContiguous);
        else
            model.reset();

        //initial turn
        stateCopy.imitate(state);
        Genome currentGenome = new WeakGenome();
        currentGenome.random(stateCopy);

        int evaluated = 0;
        double bestTurnYetFitness = Float.NEGATIVE_INFINITY;
        ArrayList<Action> bestTurnYet = null;
        double fitness;

        double UCB;
        double bestNeighbourUCB;
        Genome bestNeighbourGenome = new WeakGenome();
        Genome currentNeighbour = new WeakGenome();

        while (System.currentTimeMillis() < start + budget) {
            evaluated++;

            stateCopy.imitate(state);
            stateCopy.update(currentGenome.actions);

            //evaluate current genome with the game and add the result to the model
            fitness= evaluator.eval(stateCopy, state.p1Turn);
            model.add(currentGenome.actions, fitness);

            //add graph values
            if (visualizor != null){
                if (bestTurnYet == null || bestTurnYetFitness < fitness || !plotOverallBest) {
                    fitnesses.put(evaluated, fitness);
                    bestActions.add(new ArrayList<>(currentGenome.actions));
                } else {
                    fitnesses.put(evaluated, bestTurnYetFitness);
                    bestActions.add(bestTurnYet);
                }
            }

            //store best
            if (fitness > bestTurnYetFitness) {
                bestTurnYet = new ArrayList<>(currentGenome.actions);
                bestTurnYetFitness = fitness;
            }


            //explore neighbours
            //todo: filter duplicates somehow? use a history similar to the one in OnlineEvolution? prevent crash when setting 0 neighbours?
            //todo: create mutateFrom on genome to prevent recreating the lists
            bestNeighbourUCB = Float.NEGATIVE_INFINITY;

            for (int i = 0; i < nNeighbours; i++) {
                stateCopy.imitate(state);
                currentNeighbour.imitate(currentGenome);
                currentNeighbour.mutate(stateCopy);
                UCB = model.ucb(currentNeighbour.actions);
                if (UCB > bestNeighbourUCB){
                    bestNeighbourGenome.imitate(currentNeighbour);
                    bestNeighbourUCB = UCB;
                }
            }
            currentGenome = bestNeighbourGenome;
        }

        //final result
        turn = bestTurnYet;

        //draw graph
        if (visualizor != null){
            visualizor.p1 = state.p1Turn;
            try{
                visualizor.update();
            } catch (Exception e) {
                //e.printStackTrace();
            }
            while(visualizor.rendering){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println(title() + "-> Evaluated with game: " + evaluated + ", best value: " + bestTurnYetFitness);
    }


    @Override
    public void init(GameState state, long ms) {
        i=0;
        turn = new ArrayList<>();
        //todo: check that nothing else is needed here
    }

    @Override
    public AI copy() {
        //todo: the whole copy method
        return null;
    }

    @Override
    public String header() {
        return title()+"\n";
    }

    @Override
    public String title() {
        return "OnlineNTBEA(G)";
    }
}