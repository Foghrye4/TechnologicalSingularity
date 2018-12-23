package technological_singularity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import net.minecraft.client.util.JsonException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import technological_singularity.world.TechnologicalSingularityWorldProvider;

public class ServerProxy {

	public void registerRenders() {
	}

	public File getMinecraftDir() {
		return new File(".");
	}

	public void setSkyRenderer(TechnologicalSingularityWorldProvider provider) {
	}

	public void setCloudRenderer(TechnologicalSingularityWorldProvider provider) {
	}

	public void load() throws IOException {
	}

	public InputStream getResourceInputStream(ResourceLocation resource) throws IOException {
		String resourceURLPath = "/assets/" + resource.getResourceDomain() + "/" + resource.getResourcePath();
		return TechnologicalSingularity.class.getResourceAsStream(resourceURLPath);
	}

	public float getElevationAboveEarthSurfaceNormalized() {
		return 0f;
	}
}
