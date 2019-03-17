package technological_singularity.event;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import technological_singularity.TechnologicalSingularity;
import technological_singularity.network.ClientNetworkHandler;
import technological_singularity.player.ITSPlayer;
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
	public void onPlayerAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
		if(!(event.getObject() instanceof EntityPlayer))
			return;
		event.addCapability(ITSPlayer.CAPABILITY_KEY, new ICapabilitySerializable<NBTTagByteArray> (){

			ITSPlayer instance = TechnologicalSingularity.TSPLAYER_CAPABILITY.getDefaultInstance();
			
			@Override
			public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
				return TechnologicalSingularity.TSPLAYER_CAPABILITY == capability;
			}

			@SuppressWarnings("unchecked")
			@Override
			public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
				return TechnologicalSingularity.TSPLAYER_CAPABILITY == capability? (T)instance :null;
			}

			@Override
			public NBTTagByteArray serializeNBT() {
				return (NBTTagByteArray) TechnologicalSingularity.TSPLAYER_CAPABILITY.getStorage().writeNBT(TechnologicalSingularity.TSPLAYER_CAPABILITY, instance, null);
			}

			@Override
			public void deserializeNBT(NBTTagByteArray nbt) {
				TechnologicalSingularity.TSPLAYER_CAPABILITY.getStorage().readNBT(TechnologicalSingularity.TSPLAYER_CAPABILITY, instance, null, nbt);
			}
		});
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
		ITSPlayer tsPlayer = event.getEntity().getCapability(TechnologicalSingularity.TSPLAYER_CAPABILITY,null);
		if (tsPlayer.getShip() != null) {
			TechnologicalSingularity.network.sendShipUpdateToPlayer(tsPlayer.getShip(), player);
		}
	}
}
