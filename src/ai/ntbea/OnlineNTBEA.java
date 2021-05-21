package ai.ntbea;

import action.Action;
import action.EndTurnAction;
import action.SingletonAction;
import ai.AI;
import ai.evaluation.IStateEvaluator;
import ai.evolution.AiVisualizor;
import ai.evolution.OnlineEvolutionVisualizor;
import ai.util.ActionPruner;
import game.GameState;
import ui.UI;

import java.util.*;

//Implements the NTBEA algorithm to evolve a turn and play it
//Note that it will only work properly as p2 for now
//Note: plotting the results may worsen performance, not tested yet
//TODO: Make it work on 3-dimensional turns so it works properly on the first turn if its p1
//TODO: try starting with online evolution
//TODO: test what kFactors and eValues are adequate to properly balance exploration & exploitation
//TODO: it seems like we are playing invalid actions sometimes, check why it's happening
public class OnlineNTBEA implements AI, AiVisualizor, OnlineEAVisualizable {
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

    public OnlineNTBEA(int budget, int nNeighbours, double kFactor, double eValue, boolean use2D, boolean use3D, boolean use4D, boolean useOnlyContiguous, IStateEvaluator evaluator, Mutator mutator){
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
        //NTEBEA not run yet on this turn
        if (turn.isEmpty())
            runEA(state);

        //turn complete
        if (i>= turn.size()){
            i=0;
            turn.clear();
            return new EndTurnAction();
        }

        //play action
        final Action next = turn.get(i);
        i++;

        return next ;
    }


    private void runEA(GameState state) {
        fitnesses.clear();
        bestActions.clear();

        long start = System.currentTimeMillis();
        GameState stateCopy = new GameState(state.map);

        //create or reset the model as needed
        if (model == null)
            model = new BanditModel(5, kFactor, eValue, use2D, use3D, use4D, useOnlyContiguous);
        else
            model.reset();

        //initial turn
        stateCopy.imitate(state);
        Action[] current = getRandomValidTurn(stateCopy);

        int evaluated = 0;
        double bestTurnYetValue = Float.NEGATIVE_INFINITY;
        Action[] bestTurnYet = new Action[5];

        while (System.currentTimeMillis() < start + budget) {
            evaluated++;

            stateCopy.imitate(state);//may be not necessary
            stateCopy.update(Arrays.asList(current));//may be not necessary

            //evaluate current with the game and add the result to the model
            double fitness = evaluator.eval(stateCopy, state.p1Turn);
            model.add(current, fitness);

            //add graph values
            if (visualizor != null){
                if (evaluated == 1 || bestTurnYetValue < fitness || !plotOverallBest) {
                    fitnesses.put(evaluated, fitness);
                    bestActions.add(Arrays.stream(current).toList());
                } else {
                    fitnesses.put(evaluated, bestTurnYetValue);
                    bestActions.add(Arrays.stream(bestTurnYet).toList());
                }
            }

            //store best
            if (fitness > bestTurnYetValue) {
                bestTurnYet = current.clone();
                bestTurnYetValue = fitness;
            }


            //explore neighbours
            //todo: filter duplicates somehow? use a history similar to the one in OnlineEvolution? prevent crash when setting 0 neighbours?
            Action[] bestNeighbour = null;
            double bestNeighbourUCB = Float.NEGATIVE_INFINITY;

            for (int i = 0; i < nNeighbours; i++) {
                stateCopy.imitate(state);
                mutator.mutate(neighbourBuffer[i], current, stateCopy);
                double UCB = model.ucb(neighbourBuffer[i]);
                if (UCB > bestNeighbourUCB){
                    bestNeighbour = neighbourBuffer[i];
                    bestNeighbourUCB = UCB;
                }
            }
            current = bestNeighbour;
        }

        //final result
        turn = new ArrayList<>(Arrays.stream(bestTurnYet).toList());

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
            System.out.println(title() + "-> Evaluated with game: " + evaluated + ", best value: " + bestTurnYetValue);
        }
    }


    private Action[] getRandomValidTurn(GameState state) {
        final List<Action> possible = new ArrayList<>();
        final Action[] turn = new Action[5];
        for (int i = 0; i < 5; i++){
            if (state.isTerminal){
                turn[i] = SingletonAction.endTurnAction;
            }
            else {
                state.possibleActions(possible);
                if (possible.isEmpty()){
                    turn[i] = SingletonAction.endTurnAction;
                }
                else{
                    pruner.prune(possible, state); //todo: understand it and decide if we should use it
                    final int rnd = (int) (Math.random() * possible.size());
                    turn[i] = possible.get(rnd);
                    state.update(possible.get(rnd));
                }
            }
        }
        return turn;
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
        return "OnlineNTBEA";
    }
}