package technological_singularity.ship;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import technological_singularity.block.ShipEquipmentBlock;
import technological_singularity.block.state.IRotatable;
import technological_singularity.player.ITSPlayer;
import technological_singularity.util.TSArrayHelper;
import technological_singularity.util.TSMathHelper;
import technological_singularity.util.TSStrings;

public class Ship {
	public static final int MAX_SERIALIZED_SIZE = 4096;
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
		if(vecForward[2] < 0.0f) {
			rotationPitch = -180.0f-rotationPitch;
		}
		rotationYaw = (float) (Math.asin(vecForward[0])/Math.PI)*180f;
		rotationRoll = (float) (Math.asin(vecLeft[1])/Math.PI)*180f;
		Entity player = Minecraft.getMinecraft().getRenderViewEntity();
		player.rotationPitch = rotationPitch;
		if (player instanceof EntityAnimal) {
			EntityAnimal entityanimal = (EntityAnimal) player;
			entityanimal.rotationYawHead = rotationYaw;
		}
		if (rotationPitch < 0.0f) {
			prevRotationPitch += 360f;
			rotationPitch += 360f;
		} else if (rotationPitch > 360.0f) {
			prevRotationPitch -= 360f;
			rotationPitch -= 360f;
		}
		if (rotationYaw < 0.0f) {
			prevRotationYaw += 360f;
			rotationYaw += 360f;
		} else if (rotationYaw > 360.0f) {
			prevRotationYaw -= 360f;
			rotationYaw -= 360f;
		}
		if (rotationRoll < 0.0f) {
			prevRotationRoll += 360f;
			rotationRoll += 360f;
		} else if (rotationRoll > 360.0f) {
			prevRotationRoll -= 360f;
			rotationRoll -= 360f;
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

	public static void dropBlockPosToSet(World world, BlockPos startPos, Set<BlockPos> shipElements,
			MutableBlockPos downNorthWestCorner) {
			if(shipElements.add(startPos)){
				int[] xyz = new int[] {0,0,1,0,0,-1,0,0};
				for(int i=2;i<xyz.length;i++){
					BlockPos newPos = startPos.add(xyz[i-2], xyz[i-1], xyz[i]);
					if(newPos.getX()<downNorthWestCorner.getX())
						downNorthWestCorner.setPos(newPos.getX(),downNorthWestCorner.getY(),downNorthWestCorner.getZ());
					if(newPos.getY()<downNorthWestCorner.getY())
						downNorthWestCorner.setPos(downNorthWestCorner.getX(),newPos.getY(),downNorthWestCorner.getZ());
					if(newPos.getZ()<downNorthWestCorner.getZ())
						downNorthWestCorner.setPos(downNorthWestCorner.getX(),downNorthWestCorner.getY(),newPos.getZ());
					IBlockState state = world.getBlockState(newPos);
					if(state.getBlock() instanceof ShipEquipmentBlock){
						dropBlockPosToSet(world, newPos, shipElements, downNorthWestCorner);
					}
				}
		}
	}
	
	public void deploy(World world) {
		BlockPos com = new BlockPos(MathHelper.floor(centerOfMass.x),MathHelper.floor(centerOfMass.y),MathHelper.floor(centerOfMass.z));
		Rotation rPitch = fromAngle(rotationPitch);
		Rotation rYaw = fromAngle(rotationYaw);
		Rotation rRoll = fromAngle(rotationRoll);
		for(Equipment equipment1:equipment) {
			BlockPos lsp = equipment1.localShipPos;
			IBlockState state = equipment1.state;
			if(equipment1.state instanceof IRotatable) {
				IRotatable rs = (IRotatable) state;
				rs = rs.withRotation(Axis.X, rPitch);
				rs = rs.withRotation(Axis.Y, rYaw);
				state = rs.withRotation(Axis.Z, rRoll);
			}
			lsp = this.rotatePosAroundY(lsp, com, rYaw);
			lsp = this.rotatePosAroundX(lsp, com, rPitch);
			lsp = this.rotatePosAroundZ(lsp, com, rRoll);
			world.setBlockState(lsp.add(pilot.getPosition()), state);
		}
	}
	
	private BlockPos rotatePosAroundY(BlockPos pos, BlockPos center, Rotation rotation) {
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();

		int cx = center.getX();
		int cz = center.getZ();
		
		int dx = x - cx;
		int dz = z - cz;
		
		switch(rotation) {
		case CLOCKWISE_180:
			x = cx - dx;
			z = cz - dz;
			return new BlockPos(x,y,z);
		case CLOCKWISE_90:
			z = cz + dx;
			x = cx - dz;
			return new BlockPos(x,y,z);
		case COUNTERCLOCKWISE_90:
			z = cz - dx;
			x = cx + dz;
			return new BlockPos(x,y,z);
		case NONE:
		default:
			return pos;
		}
	}
	
	private BlockPos rotatePosAroundX(BlockPos pos, BlockPos center, Rotation rotation) {
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();

		int cy = center.getY();
		int cz = center.getZ();
		
		int dy = y - cy;
		int dz = z - cz;
		
		switch(rotation) {
		case CLOCKWISE_180:
			y = cy - dy;
			z = cz - dz;
			return new BlockPos(x,y,z);
		case CLOCKWISE_90:
			z = cz + dy;
			y = cy - dz;
			return new BlockPos(x,y,z);
		case COUNTERCLOCKWISE_90:
			z = cz - dy;
			y = cy + dz;
			return new BlockPos(x,y,z);
		case NONE:
		default:
			return pos;
		}
	}
	
	private BlockPos rotatePosAroundZ(BlockPos pos, BlockPos center, Rotation rotation) {
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();

		int cx = center.getX();
		int cy = center.getY();
		
		int dx = x - cx;
		int dy = y - cy;
		
		switch(rotation) {
		case CLOCKWISE_180:
			x = cx - dx;
			y = cy - dy;
			return new BlockPos(x,y,z);
		case CLOCKWISE_90:
			y = cy + dx;
			x = cx - dy;
			return new BlockPos(x,y,z);
		case COUNTERCLOCKWISE_90:
			y = cy - dx;
			x = cx + dy;
			return new BlockPos(x,y,z);
		case NONE:
		default:
			return pos;
		}
	}

	private Rotation fromAngle(float angle) {
		if(angle>=45.0f && angle<135.0f)
			return Rotation.CLOCKWISE_90;
		if(angle>=135.0f && angle<225.0f)
			return Rotation.CLOCKWISE_180;
		if(angle>=225.0f && angle<315.0f)
			return Rotation.COUNTERCLOCKWISE_90;
		return Rotation.NONE;
	}
}
