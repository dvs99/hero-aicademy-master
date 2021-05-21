package ai.ntbea;

import action.Action;
import action.EndTurnAction;
import ai.util.ActionPruner;
import game.GameState;

import java.util.ArrayList;
import java.util.List;


//TODO: Unit testing
public class UniformMutator implements Mutator{
    private final List<Action> possible = new ArrayList<>();
    private  final ActionPruner pruner = new ActionPruner();

    //mutates parent (without modifying it) into destination.
    //Note that it assumes destination and parent to be of equal size.
    //Note that it advances the provided GameState.
    @Override
    public void mutate(Action[] destination, Action[] parent, GameState state) {
        final int mutI = (int) (Math.random() * parent.length);
        int i =0;

        while (i< mutI){
            destination[i]=parent[i];
            state.update(parent[i]);
            i++;
        }

        state.possibleActions(possible);
        pruner.prune(possible, state); //todo: understand it and decide if we should use it
        possible.remove(parent[i]); //todo: should we keep this? It prevents regenerating the same but idk if it is worth the cost
        destination[i] = getRandomPossibleAction();
        state.update(destination[i]);
        i++;

        while (i < parent.length){
            destination[i]=parent[i];
            state.update(destination[i]);
            state.possibleActions(possible);
            pruner.prune(possible, state); //todo: understand it and decide if we should use it
            if (!possible.contains(destination[i]))
                destination[i] = getRandomPossibleAction();
            i++;
        }
    }

    private Action getRandomPossibleAction(){
        final int actionI = (int) (Math.random() * possible.size());
        if (possible.size() == 0)
            return new EndTurnAction();
        return possible.get(actionI);
    }
}
