package technological_singularity.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import technological_singularity.TechnologicalSingularity;
import technological_singularity.tileentity.ShipEquipmentTileEntity;

public class ScheduledTaskSwitchThrusterControl implements Runnable {
	private final World world;
	private final EntityPlayerMP player;
	private final BlockPos pos;
	private final int thruster;
	private final int control;

	public ScheduledTaskSwitchThrusterControl(WorldServer worldIn, EntityPlayerMP playerIn, BlockPos posIn,
			int thrusterIn, int controlIn) {
		world = worldIn;
		player = playerIn;
		pos=posIn;
		thruster=thrusterIn;
		control=controlIn;
	}


	@Override
	public void run() {
		ShipEquipmentTileEntity sete = (ShipEquipmentTileEntity) world.getTileEntity(pos);
		sete.switchThrusterControl(thruster,control);
		TechnologicalSingularity.network.sendTileEntityData(sete, player);
	}

}
