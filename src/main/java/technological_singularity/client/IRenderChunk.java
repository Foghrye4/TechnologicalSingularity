package technological_singularity.client;

import java.util.List;

public interface IRenderChunk {
	
	void updateLight();

	List<LightSourceFixedSpotlight> getLightSources();
}
