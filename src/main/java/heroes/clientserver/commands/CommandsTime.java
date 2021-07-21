package heroes.clientserver.commands;

import java.util.HashMap;
import java.util.Map;

public class CommandsTime {
    private static final Map<CommonCommands, Integer> timer = new HashMap<>();

    static { // 1000 mSec * 60 = minute
        timer.put(CommonCommands.DRAW, 1000 * 60);
        timer.put(CommonCommands.END_GAME, 1000 * 60);
        timer.put(CommonCommands.GET_ARMY, 1000 * 60 * 5);
        timer.put(CommonCommands.GET_ANSWER, 1000 * 60 * 2);
        timer.put(CommonCommands.MAX_ROOMS, 1000 * 60);
        timer.put(CommonCommands.GET_ROOM, 1000 * 60 * 2);
        timer.put(CommonCommands.FIELD_ONE, 1000 * 60);
        timer.put(CommonCommands.FIELD_TWO, 1000 * 60);
    };

    public static int getTime(final CommonCommands command) {
        return timer.get(command);
    }
}
