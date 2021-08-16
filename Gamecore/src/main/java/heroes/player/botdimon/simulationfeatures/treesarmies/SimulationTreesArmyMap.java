package heroes.player.botdimon.simulationfeatures.treesarmies;

import java.util.HashMap;
import java.util.Map;

public class SimulationTreesArmyMap {
    private static final Map<SimulationTreesArmy, SimulationTreeArmy> treesMap = new HashMap<>();

    static {
        treesMap.put(SimulationTreesArmy.SIMPLE_SIMULATION, new SimulationArmy());
    }

    public SimulationTreeArmy getTree(final SimulationTreesArmy tree) { return treesMap.get(tree); }
}
