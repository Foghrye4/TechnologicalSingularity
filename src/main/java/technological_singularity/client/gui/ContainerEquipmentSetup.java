package technological_singularity.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;
import technological_singularity.tileentity.ShipEquipmentTileEntity;

public class ContainerEquipmentSetup extends Container {

	public final ShipEquipmentTileEntity tile;
	
	public ContainerEquipmentSetup(ShipEquipmentTileEntity tileIn){
		tile = tileIn;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

	public BlockPos getPos() {
		return tile.getPos();
	}
}
