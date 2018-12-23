package technological_singularity.client;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import technological_singularity.player.ITSPlayer;
import technological_singularity.ship.Ship;

public class ClientCameraEventHandler {

	@SubscribeEvent
	public void onCameraSetup(EntityViewRenderEvent.CameraSetup event) {
		if (event.getEntity() instanceof EntityPlayerSP) {
			ITSPlayer player = (ITSPlayer) event.getEntity();
			if (player.getShip() != null) {
				Ship ship = player.getShip();
				if (Minecraft.getMinecraft().gameSettings.thirdPersonView > 0)
					GL11.glTranslatef(0.0f, 0.0f, -10.0f);
				float pitch = ((float) (ship.prevRotationPitch
						+ (ship.rotationPitch - ship.prevRotationPitch) * event.getRenderPartialTicks()));
				float yaw = ((float) (ship.prevRotationYaw
						+ (ship.rotationYaw - ship.prevRotationYaw) * event.getRenderPartialTicks()));
				float roll = ((float) (ship.prevRotationRoll
						+ (ship.rotationRoll - ship.prevRotationRoll) * event.getRenderPartialTicks()));
				
				event.setPitch(pitch);
				event.setYaw(yaw);
				event.setRoll(roll);
				event.setPitch(0.0f);
				event.setYaw(0.0f);
				event.setRoll(0.0f);
				GLU.gluLookAt(0.0f, 0.0f, 0.0f, ship.vecForward[0], ship.vecForward[1], ship.vecForward[2], ship.vecUp[0], ship.vecUp[1], ship.vecUp[2]);
			}
		}
	}
}
