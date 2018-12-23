package technological_singularity.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import technological_singularity.tileentity.ShipEquipmentTileEntity;

public class ClientGuiHandler extends ServerGuiHandler {

	@SubscribeEvent
	public void onGuiOpen(GuiOpenEvent event){
		if(!(event.getGui() instanceof GuiInventory))
			return;
		event.setCanceled(true);
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
		case 0:
			return new GuiEquipmentSetup(new ContainerEquipmentSetup((ShipEquipmentTileEntity) world.getTileEntity(new BlockPos(x,y,z))));
		case 1:
			return new GuiEngineEquipmentSetup(new ContainerEquipmentSetup((ShipEquipmentTileEntity) world.getTileEntity(new BlockPos(x,y,z))));
		}
		return null;
	}
}
