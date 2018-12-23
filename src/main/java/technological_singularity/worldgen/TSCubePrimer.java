package technological_singularity.worldgen;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cubicchunks.worldgen.generator.ICubePrimer;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.Object2ByteMap;
import it.unimi.dsi.fastutil.objects.Object2ByteOpenHashMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.NibbleArray;
import technological_singularity.block.BlockEarthStructure;
import technological_singularity.block.BlockEarthStructureSlope;
import technological_singularity.block.BlockSpaceStationHull;
import technological_singularity.init.TSBlocks;
import static technological_singularity.TechnologicalSingularity.*;

public class TSCubePrimer implements ICubePrimer {

	public static final IBlockState[] BLOCKSTATE_MAPPING = new IBlockState[256];
	public static final boolean[] IS_LIGHT_SOURCE = new boolean[256];
	public static final Object2ByteMap<IBlockState> BLOCKSTATE_REVERSE_MAPPING = new Object2ByteOpenHashMap<IBlockState>();
	public static final List<TSCubePrimer> INSTANCES = new ArrayList<TSCubePrimer>();

	public final byte[] data = new byte[4096];
	public final byte[][] lightData = new byte[81][2048];
	public final List<BlockPos> lightSources = new ArrayList<BlockPos>();
	public final String fileName;

	public static void initMapping() {
		BLOCKSTATE_MAPPING[0] = Blocks.AIR.getDefaultState();
		BLOCKSTATE_MAPPING[1] = TSBlocks.SPACE_STATION_HULL.getDefaultState();
		BLOCKSTATE_MAPPING[2] = TSBlocks.SPACE_STATION_HULL.getDefaultState()
				.withProperty(BlockSpaceStationHull.VARIANT, BlockSpaceStationHull.EnumType.HULL_STRIPED);
		BLOCKSTATE_MAPPING[3] = TSBlocks.SPACE_STATION_HULL.getDefaultState()
				.withProperty(BlockSpaceStationHull.VARIANT, BlockSpaceStationHull.EnumType.GATE);
		BLOCKSTATE_MAPPING[17] = TSBlocks.SPACE_STATION_HULL.getDefaultState();
		BLOCKSTATE_MAPPING[18] = TSBlocks.EARTH_STRUCTURE.getDefaultState();
		BLOCKSTATE_MAPPING[19] = TSBlocks.EARTH_STRUCTURE.getDefaultState()
				.withProperty(BlockEarthStructure.VARIANT, BlockEarthStructure.EnumType.BLACK_TILES);
		BLOCKSTATE_MAPPING[20] = TSBlocks.EARTH_STRUCTURE.getDefaultState()
				.withProperty(BlockEarthStructure.VARIANT, BlockEarthStructure.EnumType.HYDROCONCRETE);
		BLOCKSTATE_MAPPING[21] = TSBlocks.EARTH_STRUCTURE.getDefaultState()
				.withProperty(BlockEarthStructure.VARIANT, BlockEarthStructure.EnumType.ASPHALT);
		BLOCKSTATE_MAPPING[22] = TSBlocks.EARTH_STRUCTURE.getDefaultState()
				.withProperty(BlockEarthStructure.VARIANT, BlockEarthStructure.EnumType.ASPHALT_STRIPED);
		
		for (int meta = 0; meta < 12; meta++)
			BLOCKSTATE_MAPPING[34 + meta] = TSBlocks.EARTH_STRUCTURE_SLOPE.getDefaultState()
					.withProperty(BlockEarthStructureSlope.VARIANT, BlockEarthStructureSlope.VARIANT.fromMeta(meta));
		
		BLOCKSTATE_MAPPING[46] = Blocks.WATER.getDefaultState();
		
		for (int i = 0; i < BLOCKSTATE_MAPPING.length; i++) {
			IBlockState bstate = BLOCKSTATE_MAPPING[i];
			if (bstate != null) {
				BLOCKSTATE_REVERSE_MAPPING.put(bstate, (byte) i);
			}
		}
	}

	public TSCubePrimer(byte indexToFillDataIn) {
		fileName = null;
		for (int i = 0; i < data.length; i++) {
			data[i] = indexToFillDataIn;
		}
	}

	public TSCubePrimer(String name) {
		fileName = name;
		InputStream stream;
		try {
			stream = proxy.getResourceInputStream(new ResourceLocation(MODID, "cubes/" + name));
			stream.read(data);
			stream.close();
			for (int index = 0; index < data.length; index++) {
				if (IS_LIGHT_SOURCE[data[index]]) {
					int dx = index >>> 8;
					int dy = (index >>> 4) & 15;
					int dz = index & 15;
					this.lightSources.add(new BlockPos(dx, dy, dz));
				}
			}
			INSTANCES.add(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int lightDataIndex(int cx, int cy, int cz) {
		return ++cx * 9 + ++cy * 3 + ++cz;
	}

	@Override
	public IBlockState getBlockState(int x, int y, int z) {
		int index = x << 8 | y << 4 | z;
		return BLOCKSTATE_MAPPING[data[index]];
	}

	@Override
	public void setBlockState(int x, int y, int z, IBlockState state) {
	}
}
