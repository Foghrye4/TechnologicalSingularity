package technological_singularity.world.biome;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import technological_singularity.init.TSBiomes;

public class TSBiomeProvider extends BiomeProvider {

	public TSBiomeProvider() {
		super();
		this.getBiomesToSpawnIn().clear();
		this.getBiomesToSpawnIn().add(TSBiomes.SPACE);
	}
	
    @Nullable
    @Override
    public BlockPos findBiomePosition(int x, int z, int range, List<Biome> biomes, Random random) {
        return BlockPos.ORIGIN;
    }

	public Biome getBiome(BlockPos pos) {
		return this.getBiome(pos, (Biome) null);
	}

	public Biome getBiome(BlockPos pos, Biome defaultBiome) {
		return TSBiomes.SPACE;
	}

	public Biome[] getBiomes(@Nullable Biome[] oldBiomeList, int x, int z, int width, int depth) {
		if (oldBiomeList == null || oldBiomeList.length < width * depth) {
			oldBiomeList = new Biome[width * depth];
		}
		Arrays.fill(oldBiomeList, 0, width * depth, TSBiomes.SPACE);
		return oldBiomeList;
	}
	
    public Biome[] getBiomesForGeneration(Biome[] biomes, int x, int z, int width, int height)
    {
		Biome[] biomeList = new Biome[width * height];
		Arrays.fill(biomeList, 0, width * height, TSBiomes.SPACE);
		return biomeList;
    }
    
    public boolean areBiomesViable(int x, int z, int radius, List<Biome> allowed)
    {
    	return true;
    }
}
