package technological_singularity.client;

import java.util.Collection;
import java.util.List;

import cubicchunks.util.CubePos;
import cubicchunks.util.XYZAddressable;

public class FixedSpotLightSourceCollection implements XYZAddressable {

	private final CubePos pos;
	private final Collection<LightSourceFixedSpotlight> lightSources;
	
	public FixedSpotLightSourceCollection(CubePos posIn, Collection<LightSourceFixedSpotlight> lightSourcesIn){
		pos = posIn;
		lightSources = lightSourcesIn;
	}
	
	@Override
	public int getX() {
		return pos.getX();
	}

	@Override
	public int getY() {
		return pos.getY();
	}

	@Override
	public int getZ() {
		return pos.getZ();
	}

	public void addLightsToList(List<LightSourceFixedSpotlight> lights) {
		lights.addAll(lightSources);
	}

}
