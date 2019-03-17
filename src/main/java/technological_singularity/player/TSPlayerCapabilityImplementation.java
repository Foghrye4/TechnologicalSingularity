package technological_singularity.player;


import javax.annotation.Nullable;

import technological_singularity.ship.Ship;

public class TSPlayerCapabilityImplementation implements ITSPlayer {

	private Ship ship;
	public Ship getShip() {
		return ship;
	}
	public void setShip(@Nullable Ship shipIn) {
		ship = shipIn;
	}
}
