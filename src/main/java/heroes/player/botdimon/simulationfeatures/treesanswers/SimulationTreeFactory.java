package heroes.player.botdimon.simulationfeatures.treesanswers;

import heroes.gamelogic.Fields;
import heroes.player.botdimon.simulationfeatures.functions.IUtilityFunc;

import java.util.HashMap;
import java.util.Map;

public class SimulationTreeFactory {
    private final Map<SimulationTrees, SimulationTree> treeMap = new HashMap<>();

    private void initialize(final IUtilityFunc func, final Fields field, final int maxHeight, final boolean clustering) {
        treeMap.put(SimulationTrees.ONE_STEP_SIMULATION, new SimulationOneStep(func, field, maxHeight, clustering));
        treeMap.put(SimulationTrees.CUSTOM_STEP_SIMULATION, new SimulationCustomSteps(func, field, maxHeight, clustering));
        treeMap.put(SimulationTrees.THREAD_CUSTOM_STEP_SIMULATION, new SimulationCustomStepsWithThread(func, field, maxHeight, clustering));
        treeMap.put(SimulationTrees.EXPECTI_SIMULATION, new SimulationExpectiMax(func, field, maxHeight, clustering));
        treeMap.put(SimulationTrees.THREAD_EXPECTI_SIMULATION, new SimulationExpectiMaxWithThread(func, field, maxHeight, clustering));
    }

    public SimulationTree createSimulation(final SimulationTrees st, final IUtilityFunc func,
                                           final Fields field, final int maxHeight, final boolean clustering) {
        initialize(func, field, maxHeight, clustering);
        return treeMap.get(st);
    }

}
