package technological_singularity.network;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import technological_singularity.TechnologicalSingularity;
import technological_singularity.block.ShipEquipmentBlock;
import technological_singularity.player.ITSPlayer;
import technological_singularity.ship.EngineEquipmentType;
import technological_singularity.ship.Equipment;
import technological_singularity.ship.Ship;
import technological_singularity.ship.Thruster;
import technological_singularity.ship.ThrusterGroup;
import technological_singularity.util.TSArrayHelper;
import technological_singularity.util.TSMathHelper;

public class ScheduledTaskAssembleShip implements Runnable {

	private final World world;
	private final EntityPlayerMP player;
	private final BlockPos pos;

	public ScheduledTaskAssembleShip(World worldIn, EntityPlayerMP playerIn, BlockPos posIn) {
		world = worldIn;
		player = playerIn;
		pos=posIn;
	}

	@Override
	public void run() {
		List<Equipment> equipmentList = new ArrayList<Equipment>(); 
		Set<BlockPos> shipElements = new HashSet<BlockPos>();
		MutableBlockPos downNorthWestCorner = new MutableBlockPos();
		downNorthWestCorner.setPos(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
		Ship.dropBlockPosToSet(world, pos, shipElements, downNorthWestCorner);
		for(BlockPos pos1: shipElements){
			IBlockState state = world.getBlockState(pos1);
			ShipEquipmentBlock block = (ShipEquipmentBlock) state.getBlock();
			Equipment equipment = block.equipmentType.generateEquipment(world, state, pos1, downNorthWestCorner);
			equipmentList.add(equipment);
		}
		Ship ship = new Ship(equipmentList);
		ITSPlayer tsPlayer = player.getCapability(TechnologicalSingularity.TSPLAYER_CAPABILITY, null);
		tsPlayer.setShip(ship);
		TechnologicalSingularity.network.sendShipUpdateToPlayer(ship, player);
	}
	

}
