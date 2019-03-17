package technological_singularity.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.util.RecipeBookClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.stats.StatisticsManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import technological_singularity.TechnologicalSingularity;
import technological_singularity.client.particle.ParticleTrail;
import technological_singularity.player.ITSPlayer;
import technological_singularity.ship.Ship;
import technological_singularity.util.TSConstants;

import static technological_singularity.TechnologicalSingularity.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class ClientNetworkHandler extends ServerNetworkHandler {

	@SubscribeEvent
	public void onPacketFromServerToClient(FMLNetworkEvent.ClientCustomPacketEvent event) throws IOException {
		ByteBuf data = event.getPacket().payload();
		PacketBuffer byteBufInputStream = new PacketBuffer(data);
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayerSP player = mc.player;
		if (player == null)
			return;
		WorldClient world = mc.world;
		ITSPlayer itsPlayer = player.getCapability(TechnologicalSingularity.TSPLAYER_CAPABILITY, null);
		if (itsPlayer == null)
			return;
		switch (ClientCommands.values()[byteBufInputStream.readByte()]) {
		case UPDATE_SHIP:
			Ship ship = Ship.readFromByteBuffer(ByteBuffer.wrap(byteBufInputStream.readByteArray()));
			itsPlayer.setShip(ship);
			player.closeScreen();
			break;
		case UPDATE_TILE_ENTITY:
			BlockPos pos = byteBufInputStream.readBlockPos();
			TileEntity te = world.getTileEntity(pos);
			te.handleUpdateTag(byteBufInputStream.readCompoundTag());
			if (mc.currentScreen instanceof GuiContainer)
				mc.currentScreen.initGui();
			break;
		case CLEAR_SHIP:
			itsPlayer.setShip(null);
			player.closeScreen();
			break;
		case SPAWN_PARTICLES:
			int particleType = byteBufInputStream.readInt();
			double posX = byteBufInputStream.readDouble();
			double posY = byteBufInputStream.readDouble();
			double posZ = byteBufInputStream.readDouble();
			float velX =  byteBufInputStream.readFloat();
			float velY =  byteBufInputStream.readFloat();
			float velZ =  byteBufInputStream.readFloat();
			float startAge = byteBufInputStream.readFloat();
			switch(particleType){
			case TSConstants.PARTICLE_TYPE_TRAIL:
				ParticleTrail particle = new ParticleTrail(mc.getTextureManager(),world, posX, posY, posZ, velX, velY, velZ, startAge);
				mc.effectRenderer.addEffect(particle);
				break;
			}
			break;
		case SHOW_TIME:
			player.sendChatMessage("Client time: "+world.getTotalWorldTime());
			break;
		}
	}

	public void sendCommandAssembleShip(BlockPos pos) {
		WorldClient world = Minecraft.getMinecraft().world;
		EntityPlayerSP player = Minecraft.getMinecraft().player;
		ByteBuf bb = Unpooled.buffer(36);
		PacketBuffer byteBufOutputStream = new PacketBuffer(bb);
		byteBufOutputStream.writeByte(ServerCommands.ASSEMBLE_SHIP.ordinal());
		byteBufOutputStream.writeInt(player.getEntityId());
		byteBufOutputStream.writeInt(world.provider.getDimension());
		byteBufOutputStream.writeInt(pos.getX());
		byteBufOutputStream.writeInt(pos.getY());
		byteBufOutputStream.writeInt(pos.getZ());
		channel.sendToServer(new FMLProxyPacket(byteBufOutputStream, MODID));
	}

	public void switchThrusterControl(BlockPos pos, int thruster, int control) {
		WorldClient world = Minecraft.getMinecraft().world;
		EntityPlayerSP player = Minecraft.getMinecraft().player;
		ByteBuf bb = Unpooled.buffer(36);
		PacketBuffer byteBufOutputStream = new PacketBuffer(bb);
		byteBufOutputStream.writeByte(ServerCommands.SWITCH_THRUSTER_CONTROL.ordinal());
		byteBufOutputStream.writeInt(player.getEntityId());
		byteBufOutputStream.writeInt(world.provider.getDimension());
		byteBufOutputStream.writeInt(pos.getX());
		byteBufOutputStream.writeInt(pos.getY());
		byteBufOutputStream.writeInt(pos.getZ());
		byteBufOutputStream.writeInt(thruster);
		byteBufOutputStream.writeInt(control);
		channel.sendToServer(new FMLProxyPacket(byteBufOutputStream, MODID));
	}

	public void sendCommandSpawnParticles(int particleType, Vec3d pos, Vec3d vel, float startAge) {
		WorldClient world = Minecraft.getMinecraft().world;
		EntityPlayerSP player = Minecraft.getMinecraft().player;
		ByteBuf bb = Unpooled.buffer(36);
		PacketBuffer byteBufOutputStream = new PacketBuffer(bb);
		byteBufOutputStream.writeByte(ServerCommands.SPAWN_PARTICLES.ordinal());
		byteBufOutputStream.writeInt(player.getEntityId());
		byteBufOutputStream.writeInt(world.provider.getDimension());
		byteBufOutputStream.writeInt(particleType);
		byteBufOutputStream.writeDouble(pos.x);
		byteBufOutputStream.writeDouble(pos.y);
		byteBufOutputStream.writeDouble(pos.z);
		byteBufOutputStream.writeFloat((float) vel.x);
		byteBufOutputStream.writeFloat((float) vel.y);
		byteBufOutputStream.writeFloat((float) vel.z);
		byteBufOutputStream.writeFloat(startAge);
		channel.sendToServer(new FMLProxyPacket(byteBufOutputStream, MODID));
	}

	public void requestShipData() {
		WorldClient world = Minecraft.getMinecraft().world;
		EntityPlayerSP player = Minecraft.getMinecraft().player;
		ByteBuf bb = Unpooled.buffer(36);
		PacketBuffer byteBufOutputStream = new PacketBuffer(bb);
		byteBufOutputStream.writeByte(ServerCommands.REQUEST_SHIP_DATA.ordinal());
		byteBufOutputStream.writeInt(player.getEntityId());
		byteBufOutputStream.writeInt(world.provider.getDimension());
	}
}
