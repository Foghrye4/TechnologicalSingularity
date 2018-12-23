package technological_singularity.client;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.Iterator;

import javax.vecmath.Vector3f;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.glu.Project;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.client.shader.ShaderManager;
import net.minecraft.client.util.JsonException;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.obj.OBJModel.Face;
import net.minecraftforge.client.model.obj.OBJModel.Group;
import net.minecraftforge.client.model.obj.OBJModel.TextureCoordinate;
import net.minecraftforge.client.model.obj.OBJModel.Vertex;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import shadersmod.client.Shaders;
import technological_singularity.TechnologicalSingularity;
import technological_singularity.world.TechnologicalSingularityWorldProvider;

public class SkyRenderer extends IRenderHandler {
	
	private final int RENDER_STARS_STAGE = 0;
	private final int RENDER_EARTH_STAGE = 1;

	private final ResourceLocation sky = new ResourceLocation(TechnologicalSingularity.MODID, "textures/sky/sky.png");;
	private final ResourceLocation earthDayTextureLocation = new ResourceLocation(TechnologicalSingularity.MODID,
			"textures/object/earth_day_texture.png");;
	private final ResourceLocation ringTextureLocation = new ResourceLocation(TechnologicalSingularity.MODID,
				"textures/object/ring.png");;

	private static final FloatBuffer FOG_COLOR_BUFFER = GLAllocation.createDirectFloatBuffer(4);
	private final float skyRed1 = 0.1f;
	private final float skyRed2 = 0;
	private final float skyBlue1 = 1f;
	private final float skyBlue2 = 1f;
	private final float skyGreen1 = 0;
	private final float skyGreen2 = 0.7f;
	private final int latitudeN = 32;
	private final int longitudeN = 72;
	private final int ringSegmentLength = 32;
	private final int ringWidth = 32;
	private final int ringRadius = 512;//512;
	double[][][] geospherePoints = new double[latitudeN][longitudeN][5];
	double[][][] ringPoints = new double[longitudeN][4][5];
	private float fov = 70.0f;

	public SkyRenderer() {
		FOG_COLOR_BUFFER.clear();
		FOG_COLOR_BUFFER.put(skyRed1).put(skyGreen1).put(skyBlue1).put(1f);
		FOG_COLOR_BUFFER.flip();
		prepareGeophere();
	}

	@Override
	public void render(float partialTicks, WorldClient world, Minecraft mc) {
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
		Project.gluPerspective(fov, (float) mc.displayWidth / (float) mc.displayHeight,
				32.0f,
				TechnologicalSingularityWorldProvider.FAR_PLANE_VIEW * MathHelper.SQRT_2);
        GlStateManager.matrixMode(5888);
		this.updateShaderUniform1I("renderStage", this.RENDER_STARS_STAGE);
		float f = world.getCelestialAngle(partialTicks);
		float skyOpacity = MathHelper.cos(f * ((float) Math.PI * 2f)) * 0.5f + 0.5f;
		TechnologicalSingularityWorldProvider provider = (TechnologicalSingularityWorldProvider) world.provider;
		EntityPlayerSP player = mc.player;
		double renderPosX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) partialTicks;
		double renderPosY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) partialTicks;
		double renderPosZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) partialTicks;
		float altitude = (float) provider.getPlayerNormalAltitude(mc.getRenderViewEntity(), partialTicks);
		float sunPathRotation = provider.getSkySphereRotationInterpolatedGrad(world.getTotalWorldTime(), renderPosZ, partialTicks);
		Shaders.sunPathRotation = sunPathRotation; 
		float alpha = 1f - skyOpacity * (1f - altitude);
		float red = skyRed1 * altitude + skyRed2 * (1f - altitude);
		float green = skyGreen1 * altitude + skyGreen2 * (1f - altitude);
		float blue = skyBlue1 * altitude + skyBlue2 * (1f - altitude);
		red *= skyOpacity;
		green *= skyOpacity;
		blue *= skyOpacity;
		// GL11.glClearColor(red, green, blue, 0f);
		GL11.glDisable(GL11.GL_FOG);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder worldRendererIn = tessellator.getBuffer();
		mc.getTextureManager().bindTexture(sky);
		double skySize = TechnologicalSingularityWorldProvider.FAR_PLANE_VIEW;
		GL11.glTranslated(0, -skySize / 2d, 0);
		GL11.glRotatef(60f, 0f, 0f, 1f);
		GL11.glRotatef(sunPathRotation, 1f,	0f, 0f);
		worldRendererIn.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
		for (int ai = 0; ai < latitudeN - 1; ai++) {
			for (int li = 0; li < longitudeN; li++) {
				this.putGeoSphereVector(worldRendererIn, ai + 1, li, skySize, 1f, false);
				this.putGeoSphereVector(worldRendererIn, ai, li, skySize, 1f, false);
			}
			this.putGeoSphereVector(worldRendererIn, ai + 1, 0, skySize, 1f, true);
			this.putGeoSphereVector(worldRendererIn, ai, 0, skySize, 1f, true);
		}
		tessellator.draw();
		GL11.glPopMatrix();
		this.updateShaderUniform1I("renderStage", this.RENDER_EARTH_STAGE);
		
		// Earth
		mc.getTextureManager().bindTexture(earthDayTextureLocation);
		GL11.glPushMatrix();
		double earthSize = TechnologicalSingularityWorldProvider.EARTH_RADIUS;
		GL11.glTranslated(0,
				-earthSize - Math.max(TechnologicalSingularityWorldProvider.EARTH_ATMOSPHERE_BORDER + renderPosY, 0),
				0);
		float latitude = provider.getLatitude(renderPosX);
		float longitude = provider.getEarthLongitude(renderPosY, renderPosZ, world.getTotalWorldTime());
		GL11.glRotatef(90f + longitude, 1f, 0f, 0f);
		GL11.glRotatef(latitude, 0f, 0f, 1f);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		worldRendererIn.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
		for (int ai = 0; ai < latitudeN - 1; ai++) {
			for (int li = 0; li < longitudeN; li++) {
				this.putGeoSphereVector(worldRendererIn, ai, li, earthSize, 1f, false);
				this.putGeoSphereVector(worldRendererIn, ai + 1, li, earthSize, 1f, false);
			}
			this.putGeoSphereVector(worldRendererIn, ai, 0, earthSize, 1f, true);
			this.putGeoSphereVector(worldRendererIn, ai + 1, 0, earthSize, 1f, true);
		}
		tessellator.draw();
		GL11.glPopMatrix();
		
		// Ring close plane
		mc.getTextureManager().bindTexture(ringTextureLocation);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glPushMatrix();
		GL11.glTranslated(-renderPosX, -renderPosY, -renderPosZ % ringSegmentLength);
		worldRendererIn.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
		for (int segment = -64; segment < 64; segment++) {
			float segmentLength = ringSegmentLength * 128;
			float z1 = segment * segmentLength;
			float z2 = (segment + 1) * segmentLength;
			double y1_1 = MathHelper.sqrt(skySize * skySize - z1 * z1) - skySize + 2;
			double y1_2 = MathHelper.sqrt(skySize * skySize - z2 * z2) - skySize + 2;
			this.drawBox(0, y1_1, y1_2, z1, 31, 31, segmentLength, 0, 1, 0, 1);
		}
		tessellator.draw();
		GL11.glPopMatrix();

		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	private void putGeoSphereVector(BufferBuilder worldRendererIn, int ai, int li, double scale, float alpha,
			boolean setUTo1) {
		worldRendererIn.pos(geospherePoints[ai][li][0] * scale, geospherePoints[ai][li][1] * scale, geospherePoints[ai][li][2] * scale)
				.tex(setUTo1 ? 1d : geospherePoints[ai][li][3], geospherePoints[ai][li][4])
				.color(1.0f, 1.0f, 1.0f, alpha)
				.normal((float)geospherePoints[ai][li][0], (float)geospherePoints[ai][li][1], (float)geospherePoints[ai][li][2]).endVertex();
	}

	private void updateShaderUniform1I(String name, int v0) {
		int gp = GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM);
		if(gp == 0)
			return;
		int uniform = ARBShaderObjects.glGetUniformLocationARB(gp, name);
		if (uniform == -1)
			return;
		ARBShaderObjects.glUniform1iARB(uniform, v0);
	}

	private void prepareGeophere() {
		// Geosphere (sky, earth)
		for (int ai = 0; ai < latitudeN; ai++) {
			for (int li = 0; li < longitudeN; li++) {
				double y = ai * 2d / (latitudeN - 1) - 1.0d;
				double r = Math.sin(ai * Math.PI / (latitudeN - 1));
				double x = Math.cos((double) li / longitudeN * 2 * Math.PI) * r;
				double z = Math.sin((double) li / longitudeN * 2 * Math.PI) * r;
				double u = (double) li / longitudeN;
				double v = (double) ai / latitudeN;
				geospherePoints[ai][li][0] = x;
				geospherePoints[ai][li][1] = y;
				geospherePoints[ai][li][2] = z;
				geospherePoints[ai][li][3] = u;
				geospherePoints[ai][li][4] = v;
			}
		}
	}
	
	@SubscribeEvent
	public void fovHook(EntityViewRenderEvent.FOVModifier event){
		this.fov  = event.getFOV();
	}
	
	private void drawBox(double x1, double y1_1, double y1_2, double z1,
			double dx, double dy, double dz, 
			double u1, double u2, double v1, double v2){
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		// Bottom
		bufferbuilder.pos(x1 + dx, y1_1, z1).tex(u1, v1).color(1.0f, 1.0f, 1.0f, 1.0f).normal(0.0f, -1.0f, 0.0f).endVertex();
		bufferbuilder.pos(x1 + dx, y1_2, z1 + dz)		.tex(u1, v2).color(1.0f, 1.0f, 1.0f, 1.0f).normal(0.0f, -1.0f, 0.0f).endVertex();
		bufferbuilder.pos(x1, y1_2, z1 + dz).tex(u2, v2).color(1.0f, 1.0f, 1.0f, 1.0f).normal(0.0f, -1.0f, 0.0f).endVertex();
		bufferbuilder.pos(x1, y1_1, z1).tex(u2, v1).color(1.0f, 1.0f, 1.0f, 1.0f).normal(0.0f, -1.0f, 0.0f).endVertex();
		// Top
		bufferbuilder.pos(x1 + dx, y1_2 + dy, z1 + dz).tex(u1, v1).color(1.0f, 1.0f, 1.0f, 1.0f).normal(0.0f, 1.0f, 0.0f).endVertex();
		bufferbuilder.pos(x1 + dx, y1_1 + dy, z1).tex(u1, v2).color(1.0f, 1.0f, 1.0f, 1.0f).normal(0.0f, 1.0f, 0.0f).endVertex();
		bufferbuilder.pos(x1, y1_1 + dy, z1).tex(u2, v2).color(1.0f, 1.0f, 1.0f, 1.0f).normal(0.0f, 1.0f, 0.0f).endVertex();
		bufferbuilder.pos(x1, y1_2 + dy, z1 + dz).tex(u2, v1).color(1.0f, 1.0f, 1.0f, 1.0f).normal(0.0f, 1.0f, 0.0f).endVertex();
		// West
		bufferbuilder.pos(x1, y1_2 + dy, z1 + dz).tex(u1, v1).color(1.0f, 1.0f, 1.0f, 1.0f).normal(-1.0f, 0.0f, 0.0f).endVertex();
		bufferbuilder.pos(x1, y1_1 + dy, z1).tex(u1, v2).color(1.0f, 1.0f, 1.0f, 1.0f).normal(-1.0f, 0.0f, 0.0f).endVertex();
		bufferbuilder.pos(x1, y1_1, z1).tex(u2, v2).color(1.0f, 1.0f, 1.0f, 1.0f).normal(-1.0f, 0.0f, 0.0f).endVertex();
		bufferbuilder.pos(x1, y1_2, z1 + dz).tex(u2, v1).color(1.0f, 1.0f, 1.0f, 1.0f).normal(-1.0f, 0.0f, 0.0f).endVertex();
		// East
		bufferbuilder.pos(x1 + dx, y1_1 + dy, z1).tex(u1, v1).color(1.0f, 1.0f, 1.0f, 1.0f).normal(1.0f, 0.0f, 0.0f).endVertex();
		bufferbuilder.pos(x1 + dx, y1_2 + dy, z1 + dz).tex(u1, v2).color(1.0f, 1.0f, 1.0f, 1.0f).normal(1.0f, 0.0f, 0.0f).endVertex();
		bufferbuilder.pos(x1 + dx, y1_2, z1 + dz).tex(u2, v2).color(1.0f, 1.0f, 1.0f, 1.0f).normal(1.0f, 0.0f, 0.0f).endVertex();
		bufferbuilder.pos(x1 + dx, y1_1, z1).tex(u2, v1).color(1.0f, 1.0f, 1.0f, 1.0f).normal(1.0f, 0.0f, 0.0f).endVertex();
	}
}
