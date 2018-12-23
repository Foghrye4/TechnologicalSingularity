package technological_singularity.client;

import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class LightSourceFixedSpotlight extends LightSourceBase {
	public final float[] direction3f = new float[] {0.0f,-1.0f,0.0f};
	public int glShadowMap = -1;
	
	public void setDirection(Vec3i dirIn) {
		float d = (float) dirIn.getDistance(0, 0, 0);
		direction3f[0] = dirIn.getX()/d;
		direction3f[1] = dirIn.getY()/d;
		direction3f[2] = dirIn.getZ()/d;
	}

	public void setPos(Vec3d posIn) {
		pos = posIn;
	}
}
