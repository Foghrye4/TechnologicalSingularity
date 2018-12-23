package technological_singularity.world;

import static technological_singularity.TechnologicalSingularity.proxy;
import static technological_singularity.world.TechnologicalSingularityWorldProvider.SEA_LEVEL_Y;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;

import cubicchunks.world.ICubicWorld;
import cubicchunks.world.NotCubicChunksWorldException;
import cubicchunks.world.provider.ICubicWorldProvider;
import cubicchunks.world.type.ICubicWorldType;
import cubicchunks.worldgen.generator.ICubeGenerator;
import cubicchunks.worldgen.generator.vanilla.VanillaCompatibilityGenerator;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.Biome;
import technological_singularity.TechnologicalSingularity;
import static technological_singularity.TechnologicalSingularity.*;
import technological_singularity.init.TSBiomes;
import technological_singularity.world.biome.TSBiomeProvider;

public class TechnologicalSingularityWorldProvider extends WorldProvider implements ICubicWorldProvider {

	// I consider The Ring position to be at 40 000 meters above a sea and at Y=0.
	public static final int EARTH_ATMOSPHERE_BORDER = 4000;//Blocks, or 40 000 meters above a sea level
	public static final int SEA_LEVEL_Y = -EARTH_ATMOSPHERE_BORDER;//Blocks, or 40 000 meters above a sea level
	public static final int EARTH_RADIUS = 637100;//Blocks, or 6 371 000 meters
	public static final float FAR_PLANE_VIEW = EARTH_RADIUS + EARTH_ATMOSPHERE_BORDER;
	public static final int EARTH_EQUATRIAL_LENGTH = 4007520;//Blocks or 250470 cubes
	public static final int EARTH_EQUATRIAL_LENGTH_CUBES = EARTH_EQUATRIAL_LENGTH/16;//Blocks or 250470 cubes
	public static final int STRUCTURE_BORDER = EARTH_RADIUS + 1000;// 10 km of structures
	public static final float EARTH_TO_SUN_ORBIT_TO_GALAXY_SYMMETRY_PLANE_ANGLE = 60f;// 60 degrees
	public static final float EARTH_OBLIQUITY = 23.44f;// degrees
	public static final float RING_ROTATION_SPEED = 0.0016f;// radians per second
	public static final float EARTH_ROTATION_SPEED = 0.00010069f;// radians per second
	public static final int HEIGHTMAP_SCALE = 16;
	public static final int HEIGHTMAP_SEA_LEVEL = 216;
	private BufferedImage heightMap;
	
		
	@Override
	protected void init() {
		this.hasSkyLight = false;
		this.biomeProvider = new TSBiomeProvider();
		TechnologicalSingularity.proxy.setSkyRenderer(this);
		TechnologicalSingularity.proxy.setCloudRenderer(this);
		try {
			InputStream heightMapStream = TechnologicalSingularity.proxy.getResourceInputStream(new ResourceLocation(MODID,"heightmap/earth_heightmap.png"));
			heightMap = ImageIO.read(heightMapStream);
			heightMapStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getGeneratorTopSolidBlockHeight(int blockX, int blockZ){
		int width = heightMap.getWidth();
		int height = heightMap.getHeight();
		float pixelX = blockZ * width / (float)EARTH_EQUATRIAL_LENGTH;
		float pixelY = blockX * height / (float)EARTH_EQUATRIAL_LENGTH;
		if(pixelX<0)
			pixelX += width;
		if(pixelY<0)
			pixelY += height;
		int ipixelX = MathHelper.floor(pixelX);
		int ipixelY = MathHelper.floor(pixelY);
		int colour1 = heightMap.getRGB(ipixelX % width, ipixelY % height) & 255;
		int colourX2 = heightMap.getRGB((ipixelX + 1) % width, ipixelY % height) & 255;
		int colourY2 = heightMap.getRGB(ipixelX % width, (ipixelY + 1) % height) & 255;
		int colour2 = heightMap.getRGB((ipixelX + 1) % width, (ipixelY + 1) % height) & 255;
		float colour = colour1 + (colourX2 - colour1) * (pixelX - ipixelX) * 0.33f
				+ (colourY2 - colour1) * (pixelY - ipixelY) * 0.33f
				+ (colour2 - colour1) * (pixelY - ipixelY) * (pixelX - ipixelX) * 0.33f;
		return MathHelper.floor((colour - HEIGHTMAP_SEA_LEVEL) * HEIGHTMAP_SCALE + SEA_LEVEL_Y);
	}
	
	public float getRingRotationAngleGrad(long worldTime){
		float rotationAngleRad = worldTime * RING_ROTATION_SPEED / 20f;
		float rotationAngleGrad = (float) (rotationAngleRad / Math.PI * 180f);
		return rotationAngleGrad % 360f;
	}
	
	public float getEarthRotationAngleGrad(long worldTime){
		float rotationAngleRad = worldTime * EARTH_ROTATION_SPEED / 20f;
		float rotationAngleGrad = (float) (rotationAngleRad / Math.PI * 180f);
		return rotationAngleGrad % 360f;
	}
	
	@Override
	public float calculateCelestialAngle(long worldTime, float partialTicks) {
		return EARTH_OBLIQUITY / 360f;
	}
	
	public float getSkySphereRotationInterpolatedGrad(long worldTime, double posZ, float partialTicks) {
		double angleShift = ((posZ + EARTH_EQUATRIAL_LENGTH / 2) / EARTH_EQUATRIAL_LENGTH) % 1.0;
		angleShift *= 360.0;
		float elevationNormalized = proxy.getElevationAboveEarthSurfaceNormalized();
		float ring = this.getRingRotationAngleGrad(worldTime);
		float earth = this.getEarthRotationAngleGrad(worldTime);
		float da = ring - earth;
		if (da < -180f)
			ring += 360f;
		if (da > 180f)
			earth += 360f;
		float f = ring * elevationNormalized + earth * (1f - elevationNormalized);
		f += angleShift;
		while (f > 360f)
			f -= 360f;
		return f;
	}
	
	@Override
	public Biome getBiomeForCoords(BlockPos pos) {
		return TSBiomes.SPACE;
	}
	
	@Override
	public DimensionType getDimensionType() {
		return TechnologicalSingularity.TECHNOLOGICAL_SINGULARITY_DIMENSION_TYPE;
	}

	public double getPlayerNormalAltitude(Entity cameraEntity, float partialTicks) {
		double posY = cameraEntity.posY + (cameraEntity.prevPosY - cameraEntity.posY) * (double) (1.0F - partialTicks);
		double altitude = posY - SEA_LEVEL_Y;
		if (altitude <= 0d)
			return 0d;
		altitude /= EARTH_ATMOSPHERE_BORDER;
		if (altitude >= 1d)
			return 1d;
		return altitude;
	}
	
	public float getAltitude(double posY) {
		float altitude = (float) (posY - SEA_LEVEL_Y);
		altitude *=10.0f;
		return altitude;
	}
	
	public float getEarthLongitude(double posY, double posZ, long worldTime) {
		float ring = this.getRingRotationAngleGrad(worldTime);
		float earth = this.getEarthRotationAngleGrad(worldTime);
		float earthFromRing = ring - earth;
		float longitude = (float) (posZ*360f/EARTH_EQUATRIAL_LENGTH);
		float elevationNormalized = MathHelper.clamp((float) ((posY-SEA_LEVEL_Y)/SEA_LEVEL_Y), 0f,1f);
		return longitude + earthFromRing * elevationNormalized;
	}
	
	public float getRingLongitude(double posY, double posZ, long worldTime) {
		float ring = this.getRingRotationAngleGrad(worldTime);
		float earth = this.getEarthRotationAngleGrad(worldTime);
		float earthFromRing = ring - earth;
		float longitude = (float) (posZ*360f/EARTH_EQUATRIAL_LENGTH);
		float elevationNormalized = MathHelper.clamp((float) ((posY-SEA_LEVEL_Y)/SEA_LEVEL_Y), 0f,1f);
		return longitude + earthFromRing * (1.0f - elevationNormalized);
	}

	public float getLatitude(double posX) {
		return (float) (posX*180f/EARTH_EQUATRIAL_LENGTH);
	}

    @Nullable @Override public ICubeGenerator createCubeGenerator() {
        if (!cubicWorld().isCubicWorld()) {
            throw new NotCubicChunksWorldException();
        }
        if (this.getDimensionType() == TechnologicalSingularity.TECHNOLOGICAL_SINGULARITY_DIMENSION_TYPE && cubicWorld().getWorldType() instanceof ICubicWorldType) {
            return ((ICubicWorldType) cubicWorld().getWorldType()).createCubeGenerator(cubicWorld());
        }
        return new VanillaCompatibilityGenerator(this.createChunkGenerator(), cubicWorld());
    }
    
	private ICubicWorld cubicWorld() {
        return (ICubicWorld) world;
    }
	
	public int getOriginalActualHeight(){
		return 0;
	}
}
