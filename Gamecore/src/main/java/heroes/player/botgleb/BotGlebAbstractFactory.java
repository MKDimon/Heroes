package heroes.player.botgleb;

import java.util.Map;

public class BotGlebAbstractFactory {
    public static final Map<String, AIBot.AIBotFactory> botFactoryMap = Map.of(
            "MUL_EXPECTIMAX", new MultithreadedExpectiMaxBot.MultithreadedExpectiMaxBotFactory(),
            "EXPECTIMAX", new ExpectiMaxBot.ExpectiMaxBotFactory(),
            "MINMAX", new SimpleMinMaxBot.SimpleMinMaxBotFactory(),
            "MUL_MINMAX", new MultithreadedMinMaxBot.MultithreadedMinMaxBotFactory()
    );
}
