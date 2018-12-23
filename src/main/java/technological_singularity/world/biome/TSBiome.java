package technological_singularity.world.biome;

import net.minecraft.world.biome.Biome;
import static technological_singularity.TechnologicalSingularity.*;

public class TSBiome extends Biome {
	
	public TSBiome(BiomeProperties properties, String name) {
		super(properties);
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCaveCreatureList.clear();
		this.setRegistryName(MODID, "space");
	}
}
