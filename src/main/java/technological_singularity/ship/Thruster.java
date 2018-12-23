package technological_singularity.ship;

import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class Thruster {
	public final Vec3d direction;
	public final Vec3d comOffset;
	public final float traction;
	public final int control;
	
	public Thruster(Vec3d directions, Vec3d comOffsetIn, float tractionIn, int controlIn){
		direction=directions;
		comOffset=comOffsetIn;
		traction=tractionIn;
		control=controlIn;
	}
}
