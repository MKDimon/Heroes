package heroes.player.botdimon.simulationfeatures.treesanswers;

import heroes.gamelogic.Fields;
import heroes.player.botdimon.simulationfeatures.functions.IUtilityFunc;

import java.util.HashMap;
import java.util.Map;

public class SimulationTreeFactory {
    private final Map<SimulationTrees, SimulationTree> treeMap = new HashMap<>();

    private void initialize(final IUtilityFunc func, final Fields field, final int maxHeight) {
        treeMap.put(SimulationTrees.ONE_STEP_SIMULATION, new SimulationOneStep(func, field, maxHeight));
        treeMap.put(SimulationTrees.CUSTOM_STEP_SIMULATION, new SimulationCustomSteps(func, field, maxHeight));
        treeMap.put(SimulationTrees.THREAD_CUSTOM_STEP_SIMULATION, new SimulationCustomStepsWithThread(func, field, maxHeight));
        treeMap.put(SimulationTrees.EXPECTI_SIMULATION, new SimulationExpectiMax(func, field, maxHeight));
    }

    public SimulationTree createSimulation(final SimulationTrees st, final IUtilityFunc func,
                                           final Fields field, final int maxHeight) {
        initialize(func, field, maxHeight);
        return treeMap.get(st);
    }

}
