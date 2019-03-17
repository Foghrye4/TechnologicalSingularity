package technological_singularity.client.renderer;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.model.pipeline.ForgeBlockModelRenderer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import technological_singularity.TechnologicalSingularity;
import technological_singularity.player.ITSPlayer;
import technological_singularity.ship.Equipment;
import technological_singularity.ship.Ship;

public class PlayerRenderer {

	@SubscribeEvent
	public void onRenderPlayer(RenderPlayerEvent.Pre event) {
		ITSPlayer player = event.getEntityPlayer().getCapability(TechnologicalSingularity.TSPLAYER_CAPABILITY, null);
		if (player.getShip() == null)
			return;
		event.setCanceled(true);
		BlockRendererDispatcher brd = Minecraft.getMinecraft().getBlockRendererDispatcher();
		World world = Minecraft.getMinecraft().world;
		Ship ship = player.getShip();
		BufferBuilder buffer = Tessellator.getInstance().getBuffer();
		Vec3d com = ship.centerOfMass;
		float pitch = ((float) (ship.prevRotationPitch
				+ (ship.rotationPitch - ship.prevRotationPitch) * event.getPartialRenderTick()));
		float yaw = ((float) (ship.prevRotationYaw
				+ (ship.rotationYaw - ship.prevRotationYaw) * event.getPartialRenderTick()));
		float roll = ((float) (ship.prevRotationRoll
				+ (ship.rotationRoll - ship.prevRotationRoll) * event.getPartialRenderTick()));
        GL11.glRotatef(-yaw, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-pitch, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(-roll, 0.0F, 0.0F, 1.0F);
		GL11.glTranslated(-com.x, -com.y, -com.z);
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
		for (Equipment e : ship.equipment) {
			brd.renderBlock(e.state, e.localShipPos, world, buffer);
		}
		Tessellator.getInstance().draw();
	}
}
