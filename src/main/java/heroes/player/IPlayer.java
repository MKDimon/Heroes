package heroes.player;

import heroes.units.General;
import heroes.units.Unit;

public interface IPlayer {
    Unit[][] getArmy();

    General getGeneral();

}
