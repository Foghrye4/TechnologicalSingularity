package technological_singularity.client;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiOverlayDebug;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import technological_singularity.TechnologicalSingularity;
import technological_singularity.client.input.ClientControlInput;
import technological_singularity.player.ITSPlayer;
import technological_singularity.ship.Ship;
import technological_singularity.world.TechnologicalSingularityWorldProvider;
import static technological_singularity.TechnologicalSingularity.*;

public class HUDHandler {
	
	private final ResourceLocation texture = new ResourceLocation(MODID, "textures/gui/hud.png");
	private final int centerFrameHeight = 128;
	private final int centerFrameWidth = 128;
	private final int mouseControlHeight = 7;
	private final int mouseControlWidth = 7;
	private GuiOverlayDebug overlayDebug;
	
	@SubscribeEvent
	public void onOverlayRender(RenderGameOverlayEvent.Pre event) {
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = mc.player;
		if (!(player instanceof ITSPlayer))
			return;
		
		ITSPlayer tsPlayer = (ITSPlayer) player;
		Ship ship = tsPlayer.getShip();
		if (ship == null)
			return;

		if (this.overlayDebug == null)
			this.overlayDebug = new GuiOverlayDebug(mc);
		event.setCanceled(true);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		mc.getTextureManager().bindTexture(texture);
		ScaledResolution res = event.getResolution();
		int screenWidth = res.getScaledWidth();
		int screenHeight = res.getScaledHeight();
		int centerX = screenWidth / 2 + 1;
		int centerY = screenHeight / 2 + 1;
		mc.ingameGUI.drawTexturedModalRect(centerX - centerFrameWidth / 2, centerY - centerFrameHeight / 2, 128, 128,
				centerFrameHeight, centerFrameWidth);
		int mouseX = MathHelper.floor(ClientControlInput.mouseX * centerFrameWidth / 2);
		int mouseY = MathHelper.floor(ClientControlInput.mouseY * centerFrameHeight / 2);
		mc.ingameGUI.drawTexturedModalRect(centerX - mouseX - mouseControlHeight/2, centerY - mouseY - mouseControlWidth/2, 0, 0,
				mouseControlHeight, mouseControlWidth);
		int rotationY = MathHelper.floor(ship.movePitch * centerFrameHeight / 4);
		int rotationX = MathHelper.floor(ship.moveYaw * centerFrameWidth / 4);
		mc.ingameGUI.drawTexturedModalRect(centerX - rotationX, centerY - 3, 12, 0,
				1, 7);
		mc.ingameGUI.drawTexturedModalRect(centerX - 3, centerY - rotationY, 9, 3,
				7, 1);
		GL11.glPushMatrix();
		GL11.glTranslatef(screenWidth / 2, screenHeight / 2, 0.0f);
		GL11.glRotatef(-ship.rotationRoll, 0.0f, 0.0f, 1.0f);
		GL11.glTranslatef(-screenWidth / 2, -screenHeight / 2, 0.0f);
		int dY = 15 * 4;
		for (int i = -30;i<=30;i++) {
			int i2 = (i + 30) % 12 - 6;
			int i3 = 1 - (i + 30) / 12 % 2 * 2;
			int i4 = i2 * i3;
			int pY = MathHelper.floor(ship.rotationPitch % 360f * 4.0f);
			mc.ingameGUI.drawTexturedModalRect(centerX - 37, centerY - 3 - i * dY - pY, 181, (i4 + 6) * 6, 75, 5);
		}
		GL11.glPopMatrix();
		World world = mc.world;
		TechnologicalSingularityWorldProvider provider = (TechnologicalSingularityWorldProvider) world.provider;
		float altitude = provider.getAltitude(player.posY);
		float prevAltitude = provider.getAltitude(player.prevPosY);
		float earthLongitude = provider.getEarthLongitude(player.posY, player.posZ, world.getTotalWorldTime());
		float prevEarthLongitude = provider.getEarthLongitude(player.prevPosY, player.prevPosZ, world.getTotalWorldTime()-1);
		float ringLongitude = provider.getRingLongitude(player.posY, player.posZ, world.getTotalWorldTime());
		float prevRingLongitude = provider.getRingLongitude(player.prevPosY, player.prevPosZ, world.getTotalWorldTime()-1);
		float latitude = provider.getLatitude(player.posX);
		float prevLatitude = provider.getLatitude(player.prevPosX);
		int colour = 0xff9600;
		mc.ingameGUI.drawString(mc.fontRenderer, I18n.format(MODID + ".altitude", altitude, (altitude-prevAltitude)*20), 0, screenHeight - 10, colour);
		mc.ingameGUI.drawString(mc.fontRenderer, I18n.format(MODID + ".earthLongitude", earthLongitude, (earthLongitude-prevEarthLongitude)*20), 0, screenHeight - 20, colour);
		mc.ingameGUI.drawString(mc.fontRenderer, I18n.format(MODID + ".ringLongitude", ringLongitude, (ringLongitude-prevRingLongitude)*20), 0, screenHeight - 30, colour);
		mc.ingameGUI.drawString(mc.fontRenderer, I18n.format(MODID + ".latitude", latitude, (latitude-prevLatitude)*20), 0, screenHeight - 40, colour);
		if (mc.gameSettings.showDebugInfo)
			overlayDebug.renderDebugInfo(res);
		GL11.glPushMatrix();
        GL11.glTranslatef(0.0f, screenHeight - 48, 0.0f);
        mc.ingameGUI.getChatGUI().drawChat(mc.ingameGUI.getUpdateCounter());
		GL11.glPopMatrix();
	}
}
