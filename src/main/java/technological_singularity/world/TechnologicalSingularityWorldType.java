package technological_singularity.world;

import io.github.opencubicchunks.cubicchunks.api.util.IntRange;
import io.github.opencubicchunks.cubicchunks.api.world.ICubicWorldType;
import io.github.opencubicchunks.cubicchunks.api.worldgen.ICubeGenerator;
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
	
    public net.minecraft.world.gen.IChunkGenerator getChunkGenerator(World world, String generatorOptions)
    {
        if (this == FLAT) return new net.minecraft.world.gen.ChunkGeneratorFlat(world, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled(), generatorOptions);
        if (this == DEBUG_ALL_BLOCK_STATES) return new net.minecraft.world.gen.ChunkGeneratorDebug(world);
        if (this == CUSTOMIZED) return new net.minecraft.world.gen.ChunkGeneratorOverworld(world, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled(), generatorOptions);
        return new net.minecraft.world.gen.ChunkGeneratorOverworld(world, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled(), generatorOptions);
    }

	@Override
	public IntRange calculateGenerationHeightRange(WorldServer arg0) {
		return new IntRange(0, 256);
	}

	@Override
	public ICubeGenerator createCubeGenerator(World world) {
		return new TechnologicalSingularityTerrainGenerator(world.provider, world);
	}

	@Override
	public boolean hasCubicGeneratorForWorld(World arg0) {
		return true;
	}

}
