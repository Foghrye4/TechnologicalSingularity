package technological_singularity.client.particle;

import net.minecraftforge.fml.relauncher.SideOnly;
import technological_singularity.TechnologicalSingularity;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;

@SideOnly(Side.CLIENT)
public class ParticleTrail extends Particle {
	private static final ResourceLocation TEXTURE = new ResourceLocation(TechnologicalSingularity.MODID,"textures/particle/trail.png");
	private static final VertexFormat VERTEX_FORMAT = (new VertexFormat()).addElement(DefaultVertexFormats.POSITION_3F)
			.addElement(DefaultVertexFormats.TEX_2F).addElement(DefaultVertexFormats.COLOR_4UB)
			.addElement(DefaultVertexFormats.TEX_2S).addElement(DefaultVertexFormats.NORMAL_3B)
			.addElement(DefaultVertexFormats.PADDING_1B);
	/** The Rendering Engine. */
	private final TextureManager textureManager;
	private final float size = 0.1f;
	private final float partialAgeOffset;
	
	public ParticleTrail(TextureManager textureManagerIn, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn,
			double ySpeedIn, double zSpeedIn, float startAgeIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn);
		this.textureManager = textureManagerIn;
		this.particleMaxAge = 2;
        this.motionX = xSpeedIn;
        this.motionY = ySpeedIn;
        this.motionZ = zSpeedIn;
        this.particleAge = MathHelper.floor(startAgeIn);
        this.partialAgeOffset = startAgeIn - particleAge;
	}
	
	@Override
	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX,
			float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
		if (this.particleAge + partialAgeOffset + partialTicks <= this.particleMaxAge + 1.0f) {
			int currentFrame = MathHelper.floor((this.particleAge + partialAgeOffset + partialTicks)*64f/(this.particleMaxAge + 1.0f));
			int verticalFrame = currentFrame / 8;
			int horizontalFrame = currentFrame % 8;
			this.textureManager.bindTexture(TEXTURE);
			float u1 = horizontalFrame * 32f / 256f;
			float u2 = u1 + 32f / 256f;
			float v1 = verticalFrame * 32f / 256f;
			float v2 = v1 + 32f / 256f;
			float f4 = 2.0F * this.size;
			float f5 = (float) (this.prevPosX + (this.posX - this.prevPosX) * (double) partialTicks - interpPosX);
			float f6 = (float) (this.prevPosY + (this.posY - this.prevPosY) * (double) partialTicks - interpPosY);
			float f7 = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * (double) partialTicks - interpPosZ);
			GL11.glColor4f(0.4f, 0.4f, 0.4f, 0.6f - (this.particleAge + partialTicks) / 32.0f);
			GL11.glDisable(GL11.GL_LIGHTING);
			RenderHelper.disableStandardItemLighting();
			GL11.glDepthMask(false);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
			buffer.begin(7, VERTEX_FORMAT);
			buffer.pos((double) (f5 - rotationX * f4 - rotationXY * f4), (double) (f6 - rotationZ * f4),
					(double) (f7 - rotationYZ * f4 - rotationXZ * f4)).tex((double) u2, (double) v2)
					.color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(0, 240)
					.normal(0.0F, 1.0F, 0.0F).endVertex();
			buffer.pos((double) (f5 - rotationX * f4 + rotationXY * f4), (double) (f6 + rotationZ * f4),
					(double) (f7 - rotationYZ * f4 + rotationXZ * f4)).tex((double) u2, (double) v1)
					.color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(0, 240)
					.normal(0.0F, 1.0F, 0.0F).endVertex();
			buffer.pos((double) (f5 + rotationX * f4 + rotationXY * f4), (double) (f6 + rotationZ * f4),
					(double) (f7 + rotationYZ * f4 + rotationXZ * f4)).tex((double) u1, (double) v1)
					.color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(0, 240)
					.normal(0.0F, 1.0F, 0.0F).endVertex();
			buffer.pos((double) (f5 + rotationX * f4 - rotationXY * f4), (double) (f6 - rotationZ * f4),
					(double) (f7 + rotationYZ * f4 - rotationXZ * f4)).tex((double) u1, (double) v2)
					.color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(0, 240)
					.normal(0.0F, 1.0F, 0.0F).endVertex();
			Tessellator.getInstance().draw();
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glDepthMask(true);
		}
	}
	
	public int getBrightnessForRender(float f) {
		return 61680;
	}

	/**
	 * Retrieve what effect layer (what texture) the particle should be rendered
	 * with. 0 for the particle sprite sheet, 1 for the main Texture atlas, and
	 * 3 for a custom texture
	 */
	public int getFXLayer() {
		return 3;
	}

}
