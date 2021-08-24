package logparser.parser.statemachineparser;

import logparser.utils.Pair;

import java.util.HashMap;
import java.util.Map;

public class TokenStateMap {
    final State defaultState = new State("DEFAULT");
    final State startState = new State("GAME START");
    private final State errorState = new State("ERROR");

    private final Map<Pair<State, String>, State> states;

    public TokenStateMap() {
        states = new HashMap<>();

        final State armyTokenState = new State("ARMY");
        final State dateTokenState = new State("DATE");
        final State replayTokenState = new State("REPLAY");
        final State resultTokenState = new State("RESULT");
        final State unknownState = new State("UNKNOWN");
        final State finishState = new State("GAME END");


        states.put(new Pair<>(defaultState, "GAME START"), startState);
        states.put(new Pair<>(startState, "ARMY"), armyTokenState);
        states.put(new Pair<>(armyTokenState, "ARMY"), armyTokenState);
        states.put(new Pair<>(armyTokenState, "DATE"), dateTokenState);

        states.put(new Pair<>(dateTokenState, "REPLAY"), replayTokenState);
        states.put(new Pair<>(replayTokenState, "REPLAY"), replayTokenState);
        states.put(new Pair<>(replayTokenState, "UNKNOWN"), unknownState);
        states.put(new Pair<>(unknownState, "UNKNOWN"), unknownState);
        states.put(new Pair<>(unknownState, "RESULT"), resultTokenState);

        states.put(new Pair<>(resultTokenState, "DATE"), dateTokenState);
        states.put(new Pair<>(dateTokenState, "UNKNOWN"), unknownState);
        states.put(new Pair<>(unknownState, "GAME END"), finishState);

        states.put(new Pair<>(finishState, "GAME START"), startState);

        states.put(new Pair<>(errorState, "GAME START"), startState);

    }

    public State getStartState() {
        return defaultState;
    }

    public State getNextState(final State state, final String type) {
        return states.getOrDefault(new Pair<>(state, type), errorState);
    }



}
