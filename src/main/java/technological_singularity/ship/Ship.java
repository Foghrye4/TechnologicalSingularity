package technological_singularity.ship;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import technological_singularity.block.ShipEquipmentBlock;
import technological_singularity.player.ITSPlayer;
import technological_singularity.util.TSArrayHelper;
import technological_singularity.util.TSMathHelper;
import technological_singularity.util.TSStrings;

public class Ship {
	EntityPlayer pilot;
	public final List<Equipment> equipment = new ArrayList<Equipment>();
	ThrusterGroup[] trusterGroupsbyControl = new ThrusterGroup[12];
	public float movePitch = 0.0f;
	public float moveYaw = 0.0f;
	public float moveRoll = 0.0f;
	public float rotationYaw = 0.0f;
	public float rotationPitch = 0.0f;
	public float rotationRoll = 0.0f;
	public float prevRotationYaw = 0.0f;
	public float prevRotationPitch = 0.0f;
	public float prevRotationRoll = 0.0f;
	public float[] vecForward = new float[] {0.0f,0.0f,1.0f,1.0f};
	public float[] vecLeft = new float[] {1.0f,0.0f,0.0f,1.0f};
	public float[] vecUp = new float[] {0.0f,1.0f,0.0f,1.0f};
	public int mass = 0;
	public Vec3d centerOfMass = Vec3d.ZERO;

	public Ship(List<Equipment> equipmentList) {
		equipment.addAll(equipmentList);
		updateEquipment();
	}

	private void updateEquipment() {
		mass = 0;
		centerOfMass = Vec3d.ZERO;
		List<Thruster> thrusters = new ArrayList<Thruster>();
		List<Thruster>[] thrustersByControl = TSArrayHelper.<Thruster>createArrayOfEmptyLists(12);
		for (Equipment e : equipment) {
			float equipmentMass = e.getValueOrDefault(TSStrings.MASS, e.equipmentType.mass);
			Vec3d equipmentCenterOfMassPosition = new Vec3d(e.localShipPos.getX() + 0.5, e.localShipPos.getY() + 0.5,
					e.localShipPos.getZ() + 0.5);
			float interpolationShift = equipmentMass / (equipmentMass + mass);
			centerOfMass = TSMathHelper.interpolateBetween(centerOfMass, equipmentCenterOfMassPosition,
					interpolationShift);
			mass += equipmentMass;
		}
		for (Equipment e : equipment) {
			ShipEquipmentBlock block = (ShipEquipmentBlock) e.state.getBlock();
			if (e.equipmentType instanceof EngineEquipmentType) {
				Vec3d[] directions = block.getDirections(e.state, e.localShipPos);
				Vec3d[] positions = block.getPositions(e.state, e.localShipPos);
				EngineEquipmentType eeType = (EngineEquipmentType) block.equipmentType;
				float traction1 = e.getValueOrDefault(TSStrings.TRACTION, eeType.traction);
				int[] controls = e.controls;
				for (int i = 0; i < eeType.thrustersAmount; i++) {
					Thruster thruster = new Thruster(directions[i], positions[i].subtract(centerOfMass), traction1,
							controls[i]);
					thrusters.add(thruster);
				}
			}
		}
		for(Thruster thruster:thrusters){
			for (int b = 0, i = thruster.control; i > 0; i >>= 1, b++) {
				if((i&1)==1)
					thrustersByControl[b].add(thruster);
			}
		}
		for(int i=0;i<12;i++){
			ThrusterGroup tg = new ThrusterGroup(thrustersByControl[i]);
			this.setThrusterGroup(i,tg);
		}
	}

	public ThrusterGroup[] getThrusterGroups() {
		return trusterGroupsbyControl;
	}

	@SideOnly(Side.CLIENT)
	public void tickClient() {
		prevRotationPitch = rotationPitch;
		prevRotationYaw = rotationYaw;
		prevRotationRoll = rotationRoll;
		rotationPitch = (float) (Math.asin(vecForward[1])/Math.PI)*180f;
		rotationYaw = (float) (Math.asin(vecForward[0])/Math.PI)*180f;
		rotationRoll = (float) (Math.asin(vecLeft[1])/Math.PI)*180f;
		Entity player = Minecraft.getMinecraft().getRenderViewEntity();
		player.rotationPitch = rotationPitch;
		if (player instanceof EntityAnimal) {
			EntityAnimal entityanimal = (EntityAnimal) player;
			entityanimal.rotationYawHead = rotationYaw;
		}
	}

	public void setThrusterGroup(int i, ThrusterGroup tg) {
		trusterGroupsbyControl[i] = tg;
	}

	public void writeToByteBuffer(ByteBuffer buffer) {
		buffer.putInt(equipment.size());
		for (Equipment e : equipment) {
			e.writeToByteBuffer(buffer);
		}
	}

	public static Ship readFromByteBuffer(ByteBuffer buffer) {
		List<Equipment> equipment = new ArrayList<Equipment>();
		int equipmentLength = buffer.getInt();
		while (--equipmentLength >= 0) {
			equipment.add(Equipment.readFromByteBuffer(buffer));
		}
		return new Ship(equipment);
	}
}
