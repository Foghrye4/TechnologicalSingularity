package technological_singularity.world.biome;

import java.util.Arrays;

import javax.annotation.Nullable;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import technological_singularity.init.TSBiomes;

public class TSBiomeProvider extends BiomeProvider {

    public Biome getBiome(BlockPos pos)
    {
        return this.getBiome(pos, (Biome)null);
    }

    public Biome getBiome(BlockPos pos, Biome defaultBiome)
    {
        return TSBiomes.SPACE;
    }
    
	public Biome[] getBiomes(@Nullable Biome[] oldBiomeList, int x, int z, int width, int depth) {
		if (oldBiomeList == null || oldBiomeList.length < width * depth) {
			oldBiomeList = new Biome[width * depth];
		}
		Arrays.fill(oldBiomeList, 0, width * depth, TSBiomes.SPACE);
		return oldBiomeList;
	}
}
