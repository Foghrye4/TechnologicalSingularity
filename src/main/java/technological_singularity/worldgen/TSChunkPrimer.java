package technological_singularity.worldgen;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.github.opencubicchunks.cubicchunks.api.util.Coords;
import io.github.opencubicchunks.cubicchunks.api.worldgen.CubePrimer;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.Object2ByteMap;
import it.unimi.dsi.fastutil.objects.Object2ByteOpenHashMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.NibbleArray;
import technological_singularity.block.BlockEarthStructure;
import technological_singularity.block.BlockEarthStructureSlope;
import technological_singularity.block.BlockSpaceStationHull;
import technological_singularity.init.TSBlocks;
import static technological_singularity.TechnologicalSingularity.*;

public class TSChunkPrimer extends ChunkPrimer {
	
	private TechnologicalSingularityTerrainGenerator generator;

	public TSChunkPrimer(TechnologicalSingularityTerrainGenerator generatorIn) {
		generator = generatorIn;
	}

	@Override
	public IBlockState getBlockState(int x, int y, int z) {
		return generator.getCubePrimer(Coords.blockToCube(x), Coords.blockToCube(y), Coords.blockToCube(z))
				.getBlockState(x & 15, y & 15, z & 15);
	}

	@Override
	public void setBlockState(int x, int y, int z, IBlockState state) {
	}
}
