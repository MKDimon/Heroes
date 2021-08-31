package logparser.dto;

import java.util.List;

public class Army {
    private final String playerName;
    private final String botType;
    private final List<String> armyList;

    public Army(final String playerName, final String botType, final List<String> armyList) {
        this.playerName = playerName;
        this.botType = botType;
        this.armyList = armyList;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getBotType() {
        return botType;
    }

    public List<String> getArmyList() {
        return armyList;
    }
}
