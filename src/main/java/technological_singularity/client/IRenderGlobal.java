package technological_singularity.client;

import java.util.Collection;
import java.util.List;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public interface IRenderGlobal {
	
	void updateLightsAtRenderChunkPos(BlockPos renderChunkPos, Collection<LightSourceFixedSpotlight> lightSourcesIn);

	void addLightsToListInRange(AxisAlignedBB grow, List<LightSourceFixedSpotlight> lights);
}
