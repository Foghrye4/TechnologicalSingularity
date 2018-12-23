package technological_singularity.worldgen;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import cubicchunks.api.worldgen.biome.CubicBiome;
import cubicchunks.api.worldgen.populator.CubePopulatorEvent;
import cubicchunks.util.Box;
import cubicchunks.util.Coords;
import cubicchunks.util.CubePos;
import cubicchunks.world.column.IColumn;
import cubicchunks.world.cube.Cube;
import cubicchunks.worldgen.generator.CubeGeneratorsRegistry;
import cubicchunks.worldgen.generator.CubePrimer;
import cubicchunks.worldgen.generator.ICubeGenerator;
import cubicchunks.worldgen.generator.ICubePrimer;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.common.MinecraftForge;
import technological_singularity.world.TechnologicalSingularityWorldProvider;

public class TechnologicalSingularityTerrainGenerator implements ICubeGenerator {
	
	private final int CITY_SIZE_BIT = 3;
	private final int CITY_GRID_SIZE = 1 << CITY_SIZE_BIT;
	private final int CITY_GRID_MASK = CITY_GRID_SIZE - 1;
	private final int INVERSE_CITY_GRID_MASK = 0xffffffff << CITY_SIZE_BIT;

	private final int LEVEL_HEIGHT_BIT = 4;
	private final int LEVEL_MASK = (1 << LEVEL_HEIGHT_BIT) - 1;

	private final int LEVEL_MANHOLE_BIT = CITY_SIZE_BIT + 4;
	private final int LEVEL_MANHOLE_GRID_SIZE = 1 << LEVEL_MANHOLE_BIT;
	private final int LEVEL_MANHOLE_MASK = LEVEL_MANHOLE_GRID_SIZE - 1;

	private final int BRIDGE_RARITY = 8;

	private final Random random = new Random();
	private final List<SpawnListEntry> possibleCreatures = Lists.newArrayList();
	private final TSCubePrimer empty = new TSCubePrimer((byte) 0);
	private final TSCubePrimer water = new TSCubePrimer((byte) 46);
	private final TSCubePrimer solidStructure = new TSCubePrimer((byte) 18);
	private final TSCubePrimer solidHydroconcrete = new TSCubePrimer((byte) 20);
	private final TSCubePrimer solidAsphalt = new TSCubePrimer((byte) 21);
	
	private final TSCubePrimer manholeEast = new TSCubePrimer("manhole_east.cube_structure");
	private final TSCubePrimer manholeWest = new TSCubePrimer("manhole_west.cube_structure");
	private final TSCubePrimer manholeSouth = new TSCubePrimer("manhole_south.cube_structure");
	private final TSCubePrimer manholeNorth = new TSCubePrimer("manhole_north.cube_structure");
	
	private final TSCubePrimer ringBlock000 = new TSCubePrimer("ring_block000.cube_structure");
	private final TSCubePrimer ringBlock001 = new TSCubePrimer("ring_block001.cube_structure");
	private final TSCubePrimer ringBlock010 = new TSCubePrimer("ring_block010.cube_structure");
	private final TSCubePrimer ringBlock011 = new TSCubePrimer("ring_block011.cube_structure");
	private final TSCubePrimer ringBlock100 = new TSCubePrimer("ring_block100.cube_structure");
	private final TSCubePrimer ringBlock101 = new TSCubePrimer("ring_block101.cube_structure");
	private final TSCubePrimer ringBlock110 = new TSCubePrimer("ring_block110.cube_structure");
	private final TSCubePrimer ringBlock111 = new TSCubePrimer("ring_block111.cube_structure");
	private final TSCubePrimer ringBlock200 = new TSCubePrimer("ring_block200.cube_structure");
	private final TSCubePrimer ringBlock201 = new TSCubePrimer("ring_block201.cube_structure");
	private final TSCubePrimer ringBlock210 = new TSCubePrimer("ring_block210.cube_structure");
	private final TSCubePrimer ringBlock211 = new TSCubePrimer("ring_block211.cube_structure");
	private final TSCubePrimer ringBlock120 = new TSCubePrimer("ring_block120.cube_structure");
	private final TSCubePrimer ringBlock121 = new TSCubePrimer("ring_block121.cube_structure");
	private final TSCubePrimer ringBlock220 = new TSCubePrimer("ring_block220.cube_structure");
	private final TSCubePrimer ringBlock221 = new TSCubePrimer("ring_block221.cube_structure");
	private final TSCubePrimer[][][] ring = new TSCubePrimer[4][3][2];
	
	private final TSCubePrimer tower000 = new TSCubePrimer("tower000.cube_structure");
	private final TSCubePrimer tower001 = new TSCubePrimer("tower001.cube_structure");
	private final TSCubePrimer tower010 = new TSCubePrimer("tower010.cube_structure");
	private final TSCubePrimer tower011 = new TSCubePrimer("tower011.cube_structure");
	private final TSCubePrimer tower100 = new TSCubePrimer("tower100.cube_structure");
	private final TSCubePrimer tower110 = new TSCubePrimer("tower110.cube_structure");
	
	private final TSCubePrimer tower200 = new TSCubePrimer("tower200.cube_structure");
	private final TSCubePrimer tower201 = new TSCubePrimer("tower201.cube_structure");
	private final TSCubePrimer tower202 = new TSCubePrimer("tower202.cube_structure");
	private final TSCubePrimer tower210 = new TSCubePrimer("tower210.cube_structure");
	private final TSCubePrimer tower211 = new TSCubePrimer("tower211.cube_structure");
	private final TSCubePrimer tower212 = new TSCubePrimer("tower212.cube_structure");
	private final TSCubePrimer tower112 = new TSCubePrimer("tower112.cube_structure");
	private final TSCubePrimer tower102 = new TSCubePrimer("tower102.cube_structure");
	private final TSCubePrimer tower012 = new TSCubePrimer("tower012.cube_structure");
	private final TSCubePrimer tower002 = new TSCubePrimer("tower002.cube_structure");
	
	private final TSCubePrimer[][][] tower = new TSCubePrimer[4][2][4];

	private final TSCubePrimer towerTop00 = new TSCubePrimer("tower_top00.cube_structure");
	private final TSCubePrimer towerTop01 = new TSCubePrimer("tower_top01.cube_structure");
	private final TSCubePrimer towerTop10 = new TSCubePrimer("tower_top10.cube_structure");
	private final TSCubePrimer towerTop11 = new TSCubePrimer("tower_top11.cube_structure");
	private final TSCubePrimer towerTop02 = new TSCubePrimer("tower_top02.cube_structure");
	private final TSCubePrimer towerTop20 = new TSCubePrimer("tower_top20.cube_structure");
	private final TSCubePrimer towerTop22 = new TSCubePrimer("tower_top22.cube_structure");
	private final TSCubePrimer towerTop21 = new TSCubePrimer("tower_top21.cube_structure");
	private final TSCubePrimer towerTop12 = new TSCubePrimer("tower_top12.cube_structure");
	private final TSCubePrimer[][] towerTop = new TSCubePrimer[4][4];

	private final TSCubePrimer towerBridgeX = new TSCubePrimer("tower_bridge_x.cube_structure");
	private final TSCubePrimer towerBridgeZ = new TSCubePrimer("tower_bridge_z.cube_structure");

	@Nullable private final TechnologicalSingularityWorldProvider provider;

	public TechnologicalSingularityTerrainGenerator(WorldProvider worldProvider) {
		if(worldProvider instanceof TechnologicalSingularityWorldProvider)
			provider = (TechnologicalSingularityWorldProvider) worldProvider;
		else
			provider = null;
		ring[0][0][0] = ringBlock000;
		ring[0][0][1] = ringBlock001;
		ring[0][1][0] = ringBlock010;
		ring[0][1][1] = ringBlock011;
		ring[1][0][0] = ringBlock100;
		ring[1][0][1] = ringBlock101;
		ring[1][1][0] = ringBlock110;
		ring[1][1][1] = ringBlock111;
		ring[2][0][0] = ringBlock200;
		ring[2][0][1] = ringBlock201;
		ring[2][1][0] = ringBlock210;
		ring[2][1][1] = ringBlock211;
		ring[3][0][0] = ring[0][0][0];
		ring[3][0][1] = ring[0][0][1];
		ring[3][1][0] = ring[0][1][0];
		ring[3][1][1] = ring[0][1][1];

		ring[0][2][0] = ringBlock120;
		ring[0][2][1] = ringBlock121;
		ring[1][2][0] = ringBlock120;
		ring[1][2][1] = ringBlock121;
		ring[2][2][0] = ringBlock220;
		ring[2][2][1] = ringBlock221;
		ring[3][2][0] = ringBlock120;
		ring[3][2][1] = ringBlock121;
		
		tower[0][0][0] = tower000;
		tower[0][0][1] = tower001;
		tower[0][0][2] = tower001;
		tower[0][0][3] = tower002;
		tower[1][0][3] = tower102;
		tower[2][0][3] = tower102;
		tower[3][0][3] = tower202;
		
		tower[1][0][0] = tower100;
		tower[2][0][0] = tower100;
		tower[3][0][0] = tower200;
		tower[3][0][1] = tower201;
		tower[3][0][2] = tower201;
		tower[3][0][3] = tower202;
		
		tower[1][0][1] = empty;
		tower[2][0][1] = empty;
		tower[1][0][2] = empty;
		tower[2][0][2] = empty;
		
		tower[0][1][0] = tower010;
		tower[0][1][1] = tower011;
		tower[0][1][2] = tower011;
		tower[0][1][3] = tower012;
		tower[1][1][3] = tower112;
		tower[2][1][3] = tower112;
		tower[3][1][3] = tower212;
		
		tower[1][1][0] = tower110;
		tower[2][1][0] = tower110;
		tower[3][1][0] = tower210;
		tower[3][1][1] = tower211;
		tower[3][1][2] = tower211;
		tower[3][1][3] = tower212;
		
		tower[1][1][1] = empty;
		tower[2][1][1] = empty;
		tower[1][1][2] = empty;
		tower[2][1][2] = empty;

		towerTop[0][0] = towerTop00;
		towerTop[0][1] = towerTop01;
		towerTop[0][2] = towerTop01;
		towerTop[0][3] = towerTop02;
		
		towerTop[1][0] = towerTop10;
		towerTop[2][0] = towerTop10;
		towerTop[3][0] = towerTop20;
		
		towerTop[3][1] = towerTop21;
		towerTop[3][2] = towerTop21;
		towerTop[3][3] = towerTop22;
		
		towerTop[1][3] = towerTop12;
		towerTop[2][3] = towerTop12;
		
		towerTop[2][1] = towerTop11;
		towerTop[2][2] = towerTop11;
		towerTop[1][1] = towerTop11;
		towerTop[1][2] = towerTop11;
	}

	@Override
	public void generateColumn(IColumn arg0) {
	}

	@Override
	public ICubePrimer generateCube(int cubeX, int cubeY, int cubeZ) {
		return this.getCubePrimer(cubeX, cubeY, cubeZ);
	}
	
	private boolean isEarth(int cubeX, int cubeY, int cubeZ){
		int topBlock = this.provider.getGeneratorTopSolidBlockHeight(Coords.cubeToCenterBlock(cubeX), Coords.cubeToCenterBlock(cubeZ));
		return Coords.cubeToMinBlock(cubeY) <= topBlock;
	}
	
	private boolean isSurface(int cubeX, int cubeY, int cubeZ){
		int topBlock = this.provider.getGeneratorTopSolidBlockHeight(Coords.cubeToCenterBlock(cubeX), Coords.cubeToCenterBlock(cubeZ));
		return topBlock > TechnologicalSingularityWorldProvider.SEA_LEVEL_Y;
	}

	private boolean isTower(int cubeX, int cubeY, int cubeZ) {
		cubeX &= CITY_GRID_MASK;
		cubeZ &= CITY_GRID_MASK;
		if (cubeX > 3 || cubeZ > 3)
			return false;
		int topBlock = this.provider.getGeneratorTopSolidBlockHeight(Coords.cubeToCenterBlock(cubeX),
				Coords.cubeToCenterBlock(cubeZ));
		random.setSeed((long) cubeX >>> CITY_SIZE_BIT << 8 ^ cubeZ >>> CITY_SIZE_BIT);
		int towerHeight = random.nextInt(1024) + topBlock;
		return Coords.blockToCube(towerHeight) >= cubeY;
	}
	
	private boolean isLevelBorder(int cubeX, int cubeY, int cubeZ) {
		if (Coords.cubeToMinBlock(cubeY + 1) > TechnologicalSingularityWorldProvider.SEA_LEVEL_Y)
			return false;
		if ((-cubeY & LEVEL_MASK) != 0)
			return false;
		int levelNumber = -cubeY>>>LEVEL_HEIGHT_BIT;
		boolean isEven = (levelNumber&1) == 0;
		int manholeX = (isEven ? cubeX + LEVEL_MANHOLE_GRID_SIZE / 2 : cubeX) & LEVEL_MANHOLE_MASK;
		int manholeZ = (isEven ? cubeZ + LEVEL_MANHOLE_GRID_SIZE / 2 : cubeZ) & LEVEL_MANHOLE_MASK;
		boolean isManhole = manholeX > 1 && manholeX < CITY_GRID_SIZE - 2 && manholeZ > 1 && manholeZ < CITY_GRID_SIZE - 2;
		return !isManhole;
	}

	private TSCubePrimer getLevelBorder(int cubeX, int cubeY, int cubeZ) {
		if (!isLevelBorder(cubeX + 1, cubeY, cubeZ))
			return manholeEast;
		if (!isLevelBorder(cubeX - 1, cubeY, cubeZ))
			return manholeWest;
		if (!isLevelBorder(cubeX, cubeY, cubeZ + 1))
			return manholeSouth;
		if (!isLevelBorder(cubeX, cubeY, cubeZ - 1))
			return manholeNorth;
		return solidAsphalt;
	}
	
	private boolean isTowerBridgeX(int cubeX, int cubeY, int cubeZ) {
		int cx1 = cubeX & INVERSE_CITY_GRID_MASK;
		int cx2 = cx1 + CITY_GRID_SIZE;
		if(!isTower(cx1,cubeY,cubeZ)||!isTower(cx2,cubeY,cubeZ))
			return false;
		long seed = 3;
		seed = 41 * seed + provider.getSeed();
		seed = 41 * seed + (cubeX >>> CITY_SIZE_BIT);
		seed = 41 * seed + cubeZ;
		seed = 41 * seed + cubeY;
		random.setSeed(seed);
		return random.nextInt(BRIDGE_RARITY) == cubeY % BRIDGE_RARITY;
	}
	
	private boolean isTowerBridgeZ(int cubeX, int cubeY, int cubeZ) {
		int cz1 = cubeZ & INVERSE_CITY_GRID_MASK;
		int cz2 = cz1 + CITY_GRID_SIZE;
		if(!isTower(cubeX,cubeY,cz1)||!isTower(cubeX,cubeY,cz2))
			return false;
		long seed = 3;
		seed = 41 * seed + provider.getSeed();
		seed = 41 * seed + (cubeZ >>> CITY_SIZE_BIT);
		seed = 41 * seed + cubeX;
		seed = 41 * seed + cubeY;
		random.setSeed(seed);
		return random.nextInt(BRIDGE_RARITY) == cubeY % BRIDGE_RARITY;
	}

	
	private TSCubePrimer getTower(int cubeX, int cubeY, int cubeZ) {
		if(!this.isTower(cubeX, cubeY+1, cubeZ))
			return towerTop[cubeX&3][cubeZ&3];
		return tower[cubeX&3][cubeY&1][cubeZ&3];
	}

	private boolean isRing(int cubeX, int cubeY, int cubeZ){
		return cubeY >= 0 && cubeY <= 2 && cubeX >= 0 && cubeX <= 1;
	}
	
	private TSCubePrimer getRing(int cubeX, int cubeY, int cubeZ) {
		switch (cubeY) {
		case 0:
			switch (cubeX) {
			case 0:
				return ring[cubeZ & 3][0][0];
			case 1:
				return ring[cubeZ & 3][0][1];
			}
		case 1:
			switch (cubeX) {
			case 0:
				return ring[cubeZ & 3][1][0];
			case 1:
				return ring[cubeZ & 3][1][1];
			}
		case 2:
			switch (cubeX) {
			case 0:
				return ring[cubeZ & 3][2][0];
			case 1:
				return ring[cubeZ & 3][2][1];
			}
		default:
			break;
		}
		throw new IllegalArgumentException();
	}
	
	private boolean isCoastalDefenceWall(int cubeX, int cubeY, int cubeZ) {
		if (Coords.cubeToMinBlock(cubeY - 1) > TechnologicalSingularityWorldProvider.SEA_LEVEL_Y)
			return false;
		return isSurface(cubeX + 1, cubeY, cubeZ) 
				|| isSurface(cubeX - 1, cubeY, cubeZ)
				|| isSurface(cubeX, cubeY, cubeZ + 1) 
				|| isSurface(cubeX, cubeY, cubeZ - 1)
				|| isSurface(cubeX + 1, cubeY, cubeZ + 1) 
				|| isSurface(cubeX + 1, cubeY, cubeZ - 1)
				|| isSurface(cubeX - 1, cubeY, cubeZ + 1) 
				|| isSurface(cubeX - 1, cubeY, cubeZ - 1);
	}
	
	private boolean isOcean(int cubeX, int cubeY, int cubeZ) {
		return Coords.cubeToMinBlock(cubeY) < TechnologicalSingularityWorldProvider.SEA_LEVEL_Y;
	}


	public TSCubePrimer getCubePrimer(int cubeX, int cubeY, int cubeZ) {
		if(provider == null)
			return empty;
		if (isRing(cubeX, cubeY, cubeZ))
			return getRing(cubeX, cubeY, cubeZ);
		else if(isEarth(cubeX, cubeY, cubeZ)){
			if(isSurface(cubeX, cubeY, cubeZ)){
				if(isTower(cubeX, cubeY, cubeZ))
					return this.getTower(cubeX, cubeY, cubeZ);
				else if(isLevelBorder(cubeX, cubeY, cubeZ))
					return this.getLevelBorder(cubeX, cubeY, cubeZ);
				else if(isTowerBridgeX(cubeX, cubeY, cubeZ))
					return towerBridgeX;
				else if(isTowerBridgeZ(cubeX, cubeY, cubeZ))
					return towerBridgeZ;
			}
			else if(isCoastalDefenceWall(cubeX, cubeY, cubeZ)){
				return solidHydroconcrete;
			}
			else if(isOcean(cubeX, cubeY, cubeZ))
				return water;
		}
		return empty;
	}
	
	@Override
	public List<SpawnListEntry> getPossibleCreatures(EnumCreatureType arg0, BlockPos arg1) {
		return possibleCreatures;
	}

	@Override
	public void populate(Cube cube) {
		if (!MinecraftForge.EVENT_BUS.post(new CubePopulatorEvent(cube.getCubicWorld(), cube))) {
			CubeGeneratorsRegistry.generateWorld(cube.getCubicWorld(), new Random(cube.cubeRandomSeed()),
					cube.getCoords(),
					CubicBiome.getCubic(cube.getCubicWorld().getBiome(cube.getCoords().getCenterBlockPos())));
		}
	}

	public Box getPopulationRequirement(Cube cube) {
		return NO_POPULATOR_REQUIREMENT;
	}

	public BlockPos getClosestStructure(String name, BlockPos pos, boolean findUnexplored) {
		return BlockPos.ORIGIN;
	}

	@Override
	public void recreateStructures(Cube arg0) {
	}

	@Override
	public void recreateStructures(IColumn arg0) {
	}
}
