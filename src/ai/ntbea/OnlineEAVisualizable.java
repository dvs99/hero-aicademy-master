package ai.ntbea;

import action.Action;

import java.util.List;
import java.util.Map;

public interface OnlineEAVisualizable {
    public Map<Integer, Double> getFitnesses();
    public List<List<Action>> getBestActions();
    public void setPlotOverallBest(boolean value);
}
