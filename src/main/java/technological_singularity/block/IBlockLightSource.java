package technological_singularity.block;

import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public interface IBlockLightSource {
	Vec3i getLightSourceDirection();
	Vec3d getPos(int x, int y, int z);
}
