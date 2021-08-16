package heroes.player.botdimon.simulationfeatures.functions;

import java.util.HashMap;
import java.util.Map;

public class UtilityFuncMap {
    private static final Map<Functions, IUtilityFunc> funcMap = new HashMap<>();

    static {
        funcMap.put(Functions.SIMPLE_FUNCTION, new UtilityAnswerFuncOne());
        funcMap.put(Functions.PARAMS_FUNCTION, new UtilityAnswerFuncTwo());
        funcMap.put(Functions.DEGREE_FUNCTION, new UtilityAnswerFuncThree());
        funcMap.put(Functions.EXPONENT_FUNCTION_V1, new UtilityAnswerFuncFourV1());
        funcMap.put(Functions.EXPONENT_FUNCTION_V2, new UtilityAnswerFuncFourV2());
        funcMap.put(Functions.MONTE_CARLO, new MonteCarloFunc());
    }

    public static IUtilityFunc getFunc(Functions function) {
        return funcMap.get(function);
    }
}
