package technological_singularity.init;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.BiomeProperties;
import net.minecraftforge.registries.IForgeRegistry;
import technological_singularity.world.biome.TSBiome;

public class TSBiomes {

	public static TSBiome SPACE;

	public static void init() {
		BiomeProperties tsBiomeProperties = new Biome.BiomeProperties("Space").setRainfall(0.0f).setRainDisabled();
		SPACE = new TSBiome(tsBiomeProperties, "space");
		
	}
	
	public static void register(IForgeRegistry<Biome> registry) {
		registry.register(SPACE);
	}
}
