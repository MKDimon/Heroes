package logparser.parser.statemachineparser;

import logparser.utils.Pair;

import java.util.HashMap;
import java.util.Map;

import static logparser.parser.statemachineparser.States.*;

public class TokenStateTransitionMap {
    private final Map<Pair<States, String>, States> stateTransition;

    public TokenStateTransitionMap() {
        stateTransition = new HashMap<>();

        stateTransition.put(new Pair<>(defaultState, "GAME START"), startState);
        stateTransition.put(new Pair<>(startState, "ARMY"), armyTokenState);
        stateTransition.put(new Pair<>(armyTokenState, "ARMY"), armyTokenState);
        stateTransition.put(new Pair<>(armyTokenState, "DATE"), dateTokenState);

        stateTransition.put(new Pair<>(dateTokenState, "REPLAY"), replayTokenState);
        stateTransition.put(new Pair<>(replayTokenState, "REPLAY"), replayTokenState);
        stateTransition.put(new Pair<>(replayTokenState, "UNKNOWN"), unknownState);
        stateTransition.put(new Pair<>(unknownState, "UNKNOWN"), unknownState);
        stateTransition.put(new Pair<>(unknownState, "RESULT"), resultTokenState);

        stateTransition.put(new Pair<>(resultTokenState, "DATE"), dateTokenState);
        stateTransition.put(new Pair<>(dateTokenState, "UNKNOWN"), unknownState);
        stateTransition.put(new Pair<>(unknownState, "GAME END"), finishState);

        stateTransition.put(new Pair<>(finishState, "GAME START"), startState);

        stateTransition.put(new Pair<>(errorState, "GAME START"), startState);

    }

    public States getStartState() {
        return defaultState;
    }

    public States getNextState(final States state, final String type) {
        return stateTransition.getOrDefault(new Pair<>(state, type), errorState);
    }



}
