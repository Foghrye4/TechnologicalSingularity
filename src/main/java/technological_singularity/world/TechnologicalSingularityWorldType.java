package technological_singularity.world;

import cubicchunks.util.IntRange;
import cubicchunks.world.ICubicWorld;
import cubicchunks.world.type.ICubicWorldType;
import cubicchunks.worldgen.generator.ICubeGenerator;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeProvider;
import technological_singularity.world.biome.TSBiomeProvider;
import technological_singularity.worldgen.TechnologicalSingularityTerrainGenerator;

public class TechnologicalSingularityWorldType extends WorldType implements ICubicWorldType {

	public TechnologicalSingularityWorldType(String name) {
		super(name);
	}

	public BiomeProvider getBiomeProvider(World world) {
		return new TSBiomeProvider();
	}

	@Override
	public IntRange calculateGenerationHeightRange(WorldServer arg0) {
		return new IntRange(0, 256);
	}

	@Override
	public ICubeGenerator createCubeGenerator(ICubicWorld world) {
		return new TechnologicalSingularityTerrainGenerator(world.getProvider());
	}

}
