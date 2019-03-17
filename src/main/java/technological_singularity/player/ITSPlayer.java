package technological_singularity.player;


import javax.annotation.Nullable;

import net.minecraft.util.ResourceLocation;
import technological_singularity.TechnologicalSingularity;
import technological_singularity.ship.Ship;

public interface ITSPlayer {

	public static ResourceLocation CAPABILITY_KEY = new ResourceLocation(TechnologicalSingularity.MODID,"tsplayer");
	Ship getShip();
	void setShip(@Nullable Ship shipIn);
}
