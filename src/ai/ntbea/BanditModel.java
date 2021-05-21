package ai.ntbea;

import action.Action;

import java.util.ArrayList;
import java.util.List;

//Models the solutions space using several BanditNTuples
public class BanditModel
{
    private final ArrayList<BanditNTuple> bandits;
    List<Action[]> sampled;

    public BanditModel(int searchSpaceDimensions, double kFactor, double eValue, boolean create2D, boolean create3D, boolean create4D, boolean createOnlyContiguous) {
        sampled = new ArrayList<>();
        bandits = new ArrayList<>();

        //create 1D NTuples and add them to the bandit list
        for (int i = 0; i < searchSpaceDimensions; i++) {
            int[] posArray = new int[]{i};
            bandits.add(new BanditNTuple(posArray, kFactor, eValue));
        }

        //create 2D NTuples and add them to the bandit list
        if (create2D) {
            if (createOnlyContiguous) {
                for (int i = 0; i < searchSpaceDimensions - 1; i++) {
                    int[] posArray = new int[]{i, i+1};
                    bandits.add(new BanditNTuple(posArray, kFactor, eValue));
                }
            }
            else{
                for (int i = 0; i < searchSpaceDimensions - 1; i++) {
                    for (int j = i + 1; j < searchSpaceDimensions; j++) {
                        int[] posArray = new int[]{i, j};
                        bandits.add(new BanditNTuple(posArray, kFactor, eValue));
                    }
                }
            }
        }

        //create 3D NTuples and add them to the bandit list
        if (create3D) {
            if (createOnlyContiguous) {
                for (int i = 0; i < searchSpaceDimensions - 2; i++) {
                    int[] posArray = new int[]{i, i+1, i+2};
                    bandits.add(new BanditNTuple(posArray, kFactor, eValue));
                }
            }
            else{
                for (int i = 0; i < searchSpaceDimensions - 2; i++) {
                    for (int j = i + 1; j < searchSpaceDimensions - 1; j++) {
                        for (int k = j + 1; k < searchSpaceDimensions; k++) {
                            int[] posArray = new int[]{i, j, k};
                            bandits.add(new BanditNTuple(posArray, kFactor, eValue));
                        }
                    }
                }
            }
        }

        //create 4D NTuples and add them to the bandit list
        if (create4D) {
            if (createOnlyContiguous) {
                for (int i = 0; i < searchSpaceDimensions - 3; i++) {
                    int[] posArray = new int[]{i, i+1, i+2, i+3};
                    bandits.add(new BanditNTuple(posArray, kFactor, eValue));
                }
            }
            else{
                for (int i = 0; i < searchSpaceDimensions - 3; i++) {
                    for (int j = i + 1; j < searchSpaceDimensions - 2; j++) {
                        for (int k = j + 1; k < searchSpaceDimensions - 1; k++) {
                            for (int l = k + 1; l < searchSpaceDimensions - 1; l++) {
                                int[] posArray = new int[]{i, j, k, l};
                                bandits.add(new BanditNTuple(posArray, kFactor, eValue));
                            }
                        }
                    }
                }
            }
        }

        //Create the complete NTuple and add it to the list
        int[] posArray = new int[searchSpaceDimensions];
        for (int i = 0; i < posArray.length; i++) {
            posArray[i] = i;
        }
        bandits.add(new BanditNTuple(posArray, kFactor, eValue));
    }

    //Reset the state of the whole model
    public BanditModel reset() {
        sampled = new ArrayList<>();
        for (BanditNTuple nTuple : bandits)
            nTuple.reset();
        return this;
    }

    public void add(Action[] x, double v) {
        sampled.add(x);
        for (BanditNTuple tuple : bandits) {
            tuple.add(x, v);
        }
    }

    public double ucb(Action[] x) {
        double totalUcb = 0;
        for (BanditNTuple bandit : bandits) {
            totalUcb += bandit.ucb(x);
        }
        return  totalUcb / bandits.size();
    }

    public Action[] getBestOfSampled() {
        double maxValue = Double.NEGATIVE_INFINITY;
        Action[] bestTurn = null;
        for (Action[] a : sampled) {
            double currentValue = ucb(a);
            if (maxValue<currentValue){
                maxValue = currentValue;
                bestTurn = a;
            }
        }
        return bestTurn;
    }

    //todo: implement
    public Action[] getBestOfSampledPlusNeighbours(int nNeighbours) { return null;}
}
