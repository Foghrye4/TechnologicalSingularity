package technological_singularity.client.input;

import java.util.Arrays;
import java.util.Vector;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MouseHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.MouseInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import technological_singularity.TechnologicalSingularity;
import technological_singularity.player.ITSPlayer;
import technological_singularity.ship.Ship;
import technological_singularity.ship.ThrusterGroup;
import technological_singularity.util.TSMathHelper;

public class ClientControlInput {
	/* Thrusters by control
	 * Forward: 0
	 * Back: 1
	 * Left: 2
	 * Right: 3
	 * Sneak: 4
	 * Jump: 5
	 * Roll CW: 6
	 * Roll CCW: 7
	 * Mouse Y+: 8
	 * Mouse Y-: 9
	 * Mouse X+: 10
	 * Mouse X-: 11
	*/
	public static boolean acradeRotationType = false;
	public static float mouseX = 0.0f;
	public static float mouseY = 0.0f;
	public static final float[] reaction = new float[6];
	private final KeyBinding keyBindRollCW = new KeyBinding("key.technological_singularity.roll_cw", Keyboard.KEY_E, "key.categories.movement");
	private final KeyBinding keyBindRollCCW = new KeyBinding("key.technological_singularity.roll_ccw", Keyboard.KEY_Q, "key.categories.movement");
	
	public ClientControlInput() {
		ClientRegistry.registerKeyBinding(keyBindRollCW);
		ClientRegistry.registerKeyBinding(keyBindRollCCW);
	}
/*	
	@SubscribeEvent
	public void onEvent(KeyInputEvent event) {
		handleInput();
	}

	@SubscribeEvent
	public void onEvent(MouseInputEvent event) {
		handleInput();
	}

	private void handleInput() {
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayerSP player = Minecraft.getMinecraft().player;
		if (mc.currentScreen != null || player == null)
			return;
		ITSPlayer tsPlayer = (ITSPlayer)player;
		Ship ship = tsPlayer.getShip();
		if(ship == null)
			return;
		
	}
	*/
	@SubscribeEvent
	public void onEvent(PlayerTickEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = event.player;
		if (player != mc.player)
			return;
		if (event.side != Side.CLIENT)
			return;
		if (event.phase == Phase.START) 
			return;
		ITSPlayer tsPlayer = player.getCapability(TechnologicalSingularity.TSPLAYER_CAPABILITY, null);
		Ship ship = tsPlayer.getShip();
		Vec3d visibleVelocity = new Vec3d(player.motionX,player.motionY,player.motionZ);
		Vec3d position = player.getPositionVector();
		if(ship == null)
			return;
		GameSettings gs = mc.gameSettings;
		ThrusterGroup[] tgByControl = ship.getThrusterGroups();
		float sinRX = (float) Math.sin(ship.rotationPitch / 180f * Math.PI);
		float cosRX = (float) Math.cos(ship.rotationPitch / 180f * Math.PI);
		float sinRY = (float) Math.sin(ship.rotationYaw / 180f * Math.PI);
		float cosRY = (float) Math.cos(ship.rotationYaw / 180f * Math.PI);
		float sinRZ = (float) Math.sin(ship.rotationRoll / 180f * Math.PI);
		float cosRZ = (float) Math.cos(ship.rotationRoll / 180f * Math.PI);
        float[][] rotationMatrixX = new float[][]{
        	new float[] {1,    0,     0,0},
        	new float[] {0,cosRX,-sinRX,0},
        	new float[] {0,sinRX, cosRX,0},
        	new float[] {0,    0,    0,1}
        };
        float[][] rotationMatrixY = new float[][]{
        	new float[] {cosRY, 0,sinRY,0},
        	new float[] {0,     1,    0,0},
        	new float[] {-sinRY,0,cosRY,0},
        	new float[] {0,     0,    0,1}
        };
        float[][] rotationMatrixZ = new float[][]{
        	new float[] {cosRZ,-sinRZ,0,0},
        	new float[] {sinRZ, cosRZ,0,0},
        	new float[] {0,         0,1,0},
        	new float[] {0,         0,0,1}
        };
        float[][] rotationMatrix = TSMathHelper.multiplyMatrixes(rotationMatrixZ,rotationMatrixY);
        rotationMatrix = TSMathHelper.multiplyMatrixes(rotationMatrix,rotationMatrixX);
        float[] vecForward = new float[] {0.0f,0.0f,1.0f,1.0f};
        float[] vecUp = new float[] {0.0f,1.0f,0.0f,1.0f};
        float[] vecLeft = new float[] {1.0f,0.0f,0.0f,1.0f};
        vecForward = TSMathHelper.multiplyMatrixes(rotationMatrix, new float[][] {vecForward})[0];
        vecUp = TSMathHelper.multiplyMatrixes(rotationMatrix, new float[][] {vecUp})[0];
        vecLeft = TSMathHelper.multiplyMatrixes(rotationMatrix, new float[][] {vecLeft})[0];
        float[] vec = vecForward.clone();
		Arrays.fill(reaction, 0.0f);
		if (gs.keyBindForward.isKeyDown()) {
			tgByControl[0].getReaction(ship, reaction, rotationMatrix, 1.0f);
			tgByControl[0].spawnTrailParticles(rotationMatrix, position, visibleVelocity, 0.0f);
		}
		if (gs.keyBindBack.isKeyDown()) {
			tgByControl[1].getReaction(ship, reaction, rotationMatrix, 1.0f);
			tgByControl[1].spawnTrailParticles(rotationMatrix, position, visibleVelocity, 0.0f);
		}
		if (gs.keyBindLeft.isKeyDown()) {
			tgByControl[2].getReaction(ship, reaction, rotationMatrix, 1.0f);
			tgByControl[2].spawnTrailParticles(rotationMatrix, position, visibleVelocity, 0.0f);
		}
		if (gs.keyBindRight.isKeyDown()) {
			tgByControl[3].getReaction(ship, reaction, rotationMatrix, 1.0f);
			tgByControl[3].spawnTrailParticles(rotationMatrix, position, visibleVelocity, 0.0f);
		}
		if (gs.keyBindSneak.isKeyDown()) {
			tgByControl[4].getReaction(ship, reaction, rotationMatrix, 1.0f);
			tgByControl[4].spawnTrailParticles(rotationMatrix, position, visibleVelocity, 0.0f);
		}
		if (gs.keyBindJump.isKeyDown()) {
			tgByControl[5].getReaction(ship, reaction, rotationMatrix, 1.0f);
			tgByControl[5].spawnTrailParticles(rotationMatrix, position, visibleVelocity, 0.0f);
		}
		if (keyBindRollCW.isKeyDown()) {
			tgByControl[6].getReaction(ship, reaction, rotationMatrix, 1.0f);
			tgByControl[6].spawnTrailParticles(rotationMatrix, position, visibleVelocity, 0.0f);
		}
		if (keyBindRollCCW.isKeyDown()) {
			tgByControl[7].getReaction(ship, reaction, rotationMatrix, 1.0f);
			tgByControl[7].spawnTrailParticles(rotationMatrix, position, visibleVelocity, 0.0f);
		}
        float f = gs.mouseSensitivity * 0.6f + 0.2f;
        float mouseSensitivity = f * f * f;
		if (!acradeRotationType) {
			mouseSensitivity *= 0.01f;
		}
		mouseX -= mc.mouseHelper.deltaX * mouseSensitivity;
		mouseY += mc.mouseHelper.deltaY * mouseSensitivity;
		if (!acradeRotationType) {
			mouseX = MathHelper.clamp(mouseX, -1.0f, 1.0f);
			mouseY = MathHelper.clamp(mouseY, -1.0f, 1.0f);
		}
        if (mouseY > 0.0f) {
			tgByControl[8].getReaction(ship, reaction, rotationMatrix, mouseY);
        	tgByControl[8].spawnTrailParticles(rotationMatrix, position, visibleVelocity, mouseY);
        }
        if (mouseY < 0.0f) {
			tgByControl[9].getReaction(ship, reaction, rotationMatrix, -mouseY);
        	tgByControl[9].spawnTrailParticles(rotationMatrix, position, visibleVelocity, -mouseY);
        }
        if (mouseX > 0.0f) {
			tgByControl[10].getReaction(ship, reaction, rotationMatrix, mouseX);
        	tgByControl[10].spawnTrailParticles(rotationMatrix, position, visibleVelocity, mouseX);
        }
        if (mouseX < 0.0f) {
			tgByControl[11].getReaction(ship, reaction, rotationMatrix, -mouseX);
        	tgByControl[11].spawnTrailParticles(rotationMatrix, position, visibleVelocity, -mouseX);
        }
		if (mouseX >= 0.01f)
			mouseX -= 0.01f;
		if (mouseX <= -0.01f)
			mouseX += 0.01f;
		if (mouseY >= 0.01f)
			mouseY -= 0.01f;
		if (mouseY <= -0.01f)
			mouseY += 0.01f;
		if (mouseX < 0.01f && mouseX > -0.01f)
			mouseX = 0.0f;
		if (mouseY < 0.01f && mouseY > -0.01f)
			mouseY = 0.0f;
        player.motionX += reaction[0]/ship.mass;
        player.motionY += reaction[1]/ship.mass;
        player.motionZ += reaction[2]/ship.mass;
        ship.vecUp = TSMathHelper.rotateVec4f(ship.vecUp, ship.vecLeft, reaction[3]/ship.mass);
        ship.vecForward = TSMathHelper.rotateVec4f(ship.vecForward, ship.vecLeft, reaction[3]/ship.mass);
        ship.vecLeft = TSMathHelper.rotateVec4f(ship.vecLeft, ship.vecUp, reaction[4]/ship.mass);
        ship.vecForward = TSMathHelper.rotateVec4f(ship.vecForward, ship.vecUp, reaction[4]/ship.mass);
        ship.vecUp = TSMathHelper.rotateVec4f(ship.vecUp, ship.vecForward, reaction[5]/ship.mass);
        ship.vecLeft = TSMathHelper.rotateVec4f(ship.vecLeft, ship.vecForward, reaction[5]/ship.mass);
        System.out.println("reaction[3]="+reaction[3]);
        System.out.println("reaction[4]="+reaction[4]);
        System.out.println("reaction[5]="+reaction[5]);
        
        
		mouseX = 0.0f;
		mouseY = 0.0f;
        ship.tickClient();
	}
	
}
