package logparser.database;

import logparser.dto.Army;
import logparser.dto.DataTransferObject;
import logparser.dto.LogDate;
import logparser.dto.Result;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DTODataBaseParser {
    private final DBMSConnection dbmsConnection;

    public DTODataBaseParser(final DBMSConnection dbmsConnection) {
        this.dbmsConnection = dbmsConnection;
    }

    private int insertArmyAndReturnID(final Army army) throws SQLException {
        final Connection connection = dbmsConnection.getConnection();
        final PreparedStatement selectArmy = connection.prepareStatement(
                "select * from army where position_one = ? and position_two = ? and position_three = ?" +
                        "and position_four = ? and position_five = ? and position_six = ?"
        );
        for (int i = 0; i < army.getArmyList().size(); i++) {
            selectArmy.setString(i + 1, army.getArmyList().get(i));
        }
        final ResultSet selectResultSet = selectArmy.executeQuery();
        if (!selectResultSet.isBeforeFirst()) {
            final PreparedStatement insertArmy = connection.prepareStatement(
                    "insert into army(id, position_one, position_two, position_three, position_four," +
                            "position_five, position_six) values (nextval('MAIN_SEQ'), ?, ?, ?, ?, ?, ?)"
            );
            for (int i = 0; i < army.getArmyList().size(); i++) {
                insertArmy.setString(i + 1, army.getArmyList().get(i));
            }
            insertArmy.execute();
            final ResultSet insertResultSet = insertArmy.getGeneratedKeys();
            if (insertResultSet != null && insertResultSet.next()) {
                return insertResultSet.getInt(1);
            }
        } else {
            if (selectResultSet.next()) {
                return selectResultSet.getInt(1);
            }
        }
        return -1;
    }

    private int insertBot(final Army army) throws SQLException {
        final Connection connection = dbmsConnection.getConnection();
        final PreparedStatement selectArmy = connection.prepareStatement(
                "select * from bot where bot_type = ?"
        );
        selectArmy.setString(1, army.getBotType());
        final ResultSet selectResultSet = selectArmy.executeQuery();
        if (!selectResultSet.isBeforeFirst()) {
            final PreparedStatement insertArmy = connection.prepareStatement(
                    "insert into bot(id, bot_type) values (nextval('MAIN_SEQ'), ?)"
            );

            insertArmy.setString(1, army.getBotType());
            insertArmy.execute();
            final ResultSet insertResultSet = insertArmy.getGeneratedKeys();
            if (insertResultSet != null && insertResultSet.next()) {
                return insertResultSet.getInt(1);
            }
        } else {
            if (selectResultSet.next()) {
                return selectResultSet.getInt(1);
            }
        }
        return 0;
    }

    private int insertPlayer(final int botId, final Army army) throws SQLException {
        final Connection connection = dbmsConnection.getConnection();
        final PreparedStatement selectArmy = connection.prepareStatement(
                "select * from player where id_bot = ? and name = ?"
        );
        selectArmy.setInt(1, botId);
        selectArmy.setString(2, army.getPlayerName());
        final ResultSet selectResultSet = selectArmy.executeQuery();
        if (!selectResultSet.isBeforeFirst()) {
            final PreparedStatement insertArmy = connection.prepareStatement(
                    "insert into player(id, id_bot, name) values (nextval('MAIN_SEQ'), ?, ?)"
            );

            insertArmy.setInt(1, botId);
            insertArmy.setString(2, army.getPlayerName());
            insertArmy.execute();
            final ResultSet insertResultSet = insertArmy.getGeneratedKeys();
            if (insertResultSet != null && insertResultSet.next()) {
                return insertResultSet.getInt(1);
            }
        } else {
            if (selectResultSet.next()) {
                return selectResultSet.getInt(1);
            }
        }
        return 0;
    }

    public void insertDTO(final DataTransferObject dto) {
        try {
            final int firstArmyID = insertArmyAndReturnID(dto.getFirstArmy());
            final int secondArmyID = insertArmyAndReturnID(dto.getSecondArmy());
            final int firstPlayerID = insertPlayer(insertBot(dto.getFirstArmy()), dto.getFirstArmy());
            final int secondPlayerID = insertPlayer(insertBot(dto.getSecondArmy()), dto.getSecondArmy());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
