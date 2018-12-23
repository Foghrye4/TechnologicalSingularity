package technological_singularity.player;

import javax.annotation.Nullable;

import technological_singularity.ship.Ship;

public interface ITSPlayer {
	Ship getShip();
	void setShip(@Nullable Ship ship);
}
