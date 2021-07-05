package heroes;

import java.security.KeyPair;

public class Board {
    /*
     *          ---> X
     *          |
     *          |
     *          V Y
     */

    private int curNumRound;

    //TODO (@MKDimon) : int -> UNITS
    private int[][] fieldPlayerOne;
    private int[][] fieldPlayerTwo;

    private int generalPlayerOne;
    private int generalPlayerTwo;

    public Board(int[][] fieldPlayerOne, int[][] fieldPlayerTwo, int generalPlayerOne, int generalPlayerTwo) { //TODO: Validation
        curNumRound = 1;

        this.fieldPlayerOne = fieldPlayerOne;
        this.fieldPlayerTwo = fieldPlayerTwo;
        this.generalPlayerOne = generalPlayerOne;
        this.generalPlayerTwo = generalPlayerTwo;
    }

    private int getUnitByCoordinate(int x, int y, boolean field) { //TODO: Validation
        if (!field) {
            return fieldPlayerOne[x][y];
        }
        else {
            return fieldPlayerTwo[x][y];
        }
    }

}
