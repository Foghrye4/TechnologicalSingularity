package technological_singularity.event;

import java.nio.ByteBuffer;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import technological_singularity.TechnologicalSingularity;
import technological_singularity.network.ClientNetworkHandler;
import technological_singularity.player.ITSPlayer;
import technological_singularity.ship.Ship;
import technological_singularity.util.TSStrings;
import technological_singularity.world.TechnologicalSingularityWorldType;

public class PlayerEventHandler {

	@SubscribeEvent
	public void onPlayerConstructingEvent(EntityEvent.EntityConstructing event) {
		if (event.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntity();
			if (player.getEntityWorld() != null
					&& player.getEntityWorld().getWorldType() instanceof TechnologicalSingularityWorldType) {
				player.dimension = TechnologicalSingularity.TECHNOLOGICAL_SINGULARITY_DIMENSION_ID;
				player.setSpawnDimension(TechnologicalSingularity.TECHNOLOGICAL_SINGULARITY_DIMENSION_ID);
			}
		}
	}

	@SubscribeEvent
	public void onPlayerLoading(LoadFromFile event) {
		if (!(event.getEntity() instanceof EntityPlayerMP))
			return;
		EntityPlayerMP player = (EntityPlayerMP) event.getEntity();
		ITSPlayer tsPlayer = (ITSPlayer) player;
		if (player.getEntityData().hasKey(TSStrings.NBT_SHIP)) {
			Ship ship = Ship
					.readFromByteBuffer(ByteBuffer.wrap(player.getEntityData().getByteArray(TSStrings.NBT_SHIP)));
			tsPlayer.setShip(ship);
		}
	}

	@SubscribeEvent
	public void onPlayerSaving(SaveToFile event) {
		if (!(event.getEntity() instanceof EntityPlayerMP))
			return;
		EntityPlayerMP player = (EntityPlayerMP) event.getEntity();
		ITSPlayer tsPlayer = (ITSPlayer) player;
		if (tsPlayer.getShip() != null) {
			ByteBuffer byteBuffer = ByteBuffer.allocate(2048);
			tsPlayer.getShip().writeToByteBuffer(byteBuffer);
			byteBuffer.limit(byteBuffer.position());
			player.getEntityData().setByteArray(TSStrings.NBT_SHIP, byteBuffer.array());
		} else {
			player.getEntityData().removeTag(TSStrings.NBT_SHIP);
		}
		if (player.getEntityData().hasKey(TSStrings.NBT_SHIP)) {
			Ship ship = Ship
					.readFromByteBuffer(ByteBuffer.wrap(player.getEntityData().getByteArray(TSStrings.NBT_SHIP)));
			tsPlayer.setShip(ship);
		}
	}

	@SubscribeEvent
	public void onPlayerJoinWorldEvent(EntityJoinWorldEvent event) {
		if (!(event.getWorld().getWorldType() instanceof TechnologicalSingularityWorldType))
			return;
		if (event.getWorld().isRemote && event.getEntity() == Minecraft.getMinecraft().player) {
			ClientNetworkHandler network = (ClientNetworkHandler) TechnologicalSingularity.network;
			network.requestShipData();
		}
		if (!(event.getEntity() instanceof EntityPlayerMP))
			return;
		EntityPlayerMP player = (EntityPlayerMP) event.getEntity();
		ITSPlayer tsPlayer = (ITSPlayer) player;
		if (tsPlayer.getShip() != null) {
			TechnologicalSingularity.network.sendShipUpdateToPlayer(tsPlayer.getShip(), player);
		}
	}
}
