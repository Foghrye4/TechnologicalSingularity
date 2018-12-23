package technological_singularity.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import technological_singularity.player.ITSPlayer;
import technological_singularity.ship.Ship;

import static technological_singularity.TechnologicalSingularity.*;

import java.io.IOException;
import java.nio.ByteBuffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;

public class ServerNetworkHandler {

	public enum ClientCommands {
		UPDATE_SHIP, UPDATE_TILE_ENTITY, CLEAR_SHIP, SPAWN_PARTICLES, SHOW_TIME;
	}

	public enum ServerCommands {
		ASSEMBLE_SHIP, SWITCH_THRUSTER_CONTROL, SPAWN_PARTICLES, REQUEST_SHIP_DATA;
	}

	protected static FMLEventChannel channel;
	private MinecraftServer server;

	public void load() {
		if (channel == null) {
			channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(MODID);
			channel.register(this);
		}
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onPacketFromClientToServer(FMLNetworkEvent.ServerCustomPacketEvent event) throws IOException {
		ByteBuf data = event.getPacket().payload();
		PacketBuffer byteBufInputStream = new PacketBuffer(data);
		int playerEntityId;
		int worldDimensionId;
		int blockPosX, blockPosY, blockPosZ;
		WorldServer world;
		EntityPlayerMP player;
		ByteBuf bb;
		PacketBuffer byteBufOutputStream;
		switch (ServerCommands.values()[byteBufInputStream.readByte()]) {
		case ASSEMBLE_SHIP:
			playerEntityId = byteBufInputStream.readInt();
			worldDimensionId = byteBufInputStream.readInt();
			world = server.getWorld(worldDimensionId);
			player = (EntityPlayerMP) world.getEntityByID(playerEntityId);
			blockPosX = byteBufInputStream.readInt();
			blockPosY = byteBufInputStream.readInt();
			blockPosZ = byteBufInputStream.readInt();
			world.addScheduledTask(
					new ScheduledTaskAssembleShip(world, player, new BlockPos(blockPosX, blockPosY, blockPosZ)));
			break;
		case SWITCH_THRUSTER_CONTROL:
			playerEntityId = byteBufInputStream.readInt();
			worldDimensionId = byteBufInputStream.readInt();
			world = server.getWorld(worldDimensionId);
			player = (EntityPlayerMP) world.getEntityByID(playerEntityId);
			blockPosX = byteBufInputStream.readInt();
			blockPosY = byteBufInputStream.readInt();
			blockPosZ = byteBufInputStream.readInt();
			int thruster = byteBufInputStream.readInt();
			int control = byteBufInputStream.readInt();
			world.addScheduledTask(new ScheduledTaskSwitchThrusterControl(world, player,
					new BlockPos(blockPosX, blockPosY, blockPosZ), thruster, control));
			break;
		case SPAWN_PARTICLES:
			playerEntityId = byteBufInputStream.readInt();
			worldDimensionId = byteBufInputStream.readInt();
			world = server.getWorld(worldDimensionId);
			player = (EntityPlayerMP) world.getEntityByID(playerEntityId);
			bb = Unpooled.buffer(36);
			byteBufOutputStream = new PacketBuffer(bb);
			byteBufOutputStream.writeByte(ClientCommands.SPAWN_PARTICLES.ordinal());
			byteBufOutputStream.writeBytes(byteBufInputStream, byteBufInputStream.readerIndex(),
					byteBufInputStream.readableBytes());
			channel.sendToAllAround(new FMLProxyPacket(byteBufOutputStream, MODID),
					new TargetPoint(worldDimensionId, player.posX, player.posY, player.posZ, 128.0d));
			break;
		case REQUEST_SHIP_DATA:
			playerEntityId = byteBufInputStream.readInt();
			worldDimensionId = byteBufInputStream.readInt();
			world = server.getWorld(worldDimensionId);
			player = (EntityPlayerMP) world.getEntityByID(playerEntityId);
			ITSPlayer itsPlayer = (ITSPlayer) player;
			if (itsPlayer.getShip() != null) {
				FMLProxyPacket packet = this.prepareShipUpdatePacket(itsPlayer.getShip(), player);
				channel.sendToAllAround(packet,
						new TargetPoint(worldDimensionId, player.posX, player.posY, player.posZ, 128.0d));
			}
		}
	}

	public void setServer(MinecraftServer serverIn) {
		this.server = serverIn;
	}
	
	private FMLProxyPacket prepareShipUpdatePacket(Ship ship, EntityPlayerMP player){
		ByteBuf bb = Unpooled.buffer(36);
		PacketBuffer byteBufOutputStream = new PacketBuffer(bb);
		byteBufOutputStream.writeByte(ClientCommands.UPDATE_SHIP.ordinal());
		ByteBuffer byteBuffer = ByteBuffer.allocate(2048);
		ship.writeToByteBuffer(byteBuffer);
		byteBuffer.limit(byteBuffer.position());
		byteBufOutputStream.writeByteArray(byteBuffer.array());
		return new FMLProxyPacket(byteBufOutputStream, MODID);
	}

	public void sendShipUpdateToPlayer(Ship ship, EntityPlayerMP player) {
		FMLProxyPacket packet = this.prepareShipUpdatePacket(ship, player);
		channel.sendTo(packet, player);
	}

	public void sendTileEntityData(TileEntity te, EntityPlayerMP player) {
		ByteBuf bb = Unpooled.buffer(36);
		PacketBuffer byteBufOutputStream = new PacketBuffer(bb);
		byteBufOutputStream.writeByte(ClientCommands.UPDATE_TILE_ENTITY.ordinal());
		byteBufOutputStream.writeBlockPos(te.getPos());
		byteBufOutputStream.writeCompoundTag(te.getUpdateTag());
		channel.sendTo(new FMLProxyPacket(byteBufOutputStream, MODID), player);
	}

	public void sendCommand(EntityPlayerMP player, ClientCommands command) {
		ByteBuf bb = Unpooled.buffer(36);
		PacketBuffer byteBufOutputStream = new PacketBuffer(bb);
		byteBufOutputStream.writeByte(command.ordinal());
		channel.sendTo(new FMLProxyPacket(byteBufOutputStream, MODID), player);
	}
}
