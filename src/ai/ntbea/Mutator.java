package ai.ntbea;


import action.Action;
import game.GameState;

public interface Mutator
{
    void mutate(Action[] destination, Action[] parent, GameState state);
}
