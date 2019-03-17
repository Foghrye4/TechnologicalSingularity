package technological_singularity.command;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import io.github.opencubicchunks.cubicchunks.api.util.CubePos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import technological_singularity.TechnologicalSingularity;
import technological_singularity.network.ServerNetworkHandler.ClientCommands;
import technological_singularity.worldgen.TSCubePrimer;

public class TSCommands extends CommandBase {

	@Override
	public String getName() {
		return "ts";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/ts {place|write|info} cube_name";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		File file = null;
		if (args.length > 1) {
			String cubeS = args[1];
			if (!cubeS.contains(".cube_structure")) {
				cubeS = cubeS + ".cube_structure";
			}
			file = getFile("cubes", cubeS);
		}
		World world = sender.getEntityWorld();
		BlockPos pos = CubePos.fromBlockCoords(sender.getPosition()).getMinBlockPos();
		try {
			if (args[0].equalsIgnoreCase("place")) {
				int index = 0;
				InputStream is = new FileInputStream(file);
				DataInputStream dis = new DataInputStream(is);
				boolean rotateCV = false;
				boolean mirrorX = false;
				boolean mirrorY = false;
				boolean mirrorZ = false;
				if (args.length == 6) {
					mirrorX = Boolean.valueOf(args[2]);
					mirrorY = Boolean.valueOf(args[3]);
					mirrorZ = Boolean.valueOf(args[4]);
					rotateCV = Boolean.valueOf(args[5]);
				}
				if (args.length == 5) {
					int offsetX = Integer.valueOf(args[2]);
					int offsetY = Integer.valueOf(args[3]);
					int offsetZ = Integer.valueOf(args[4]);
					pos = pos.add(offsetX, offsetY, offsetZ);
				}
				this.writeStructureToEBS(world, pos, index, dis, rotateCV, mirrorX, mirrorY, mirrorZ);
				dis.close();
				is.close();
			} else if (args[0].equalsIgnoreCase("write")) {
				ByteBuffer bf = ByteBuffer.allocate(4096);
				CubePos cpos = CubePos.fromBlockCoords(pos);
				for (int x = cpos.getMinBlockX(); x <= cpos.getMaxBlockX(); x++)
					for (int y = cpos.getMinBlockY(); y <= cpos.getMaxBlockY(); y++)
						for (int z = cpos.getMinBlockZ(); z <= cpos.getMaxBlockZ(); z++) {
							byte bsid = 0;
							IBlockState bs = world.getBlockState(new BlockPos(x, y, z));
							if (TSCubePrimer.BLOCKSTATE_REVERSE_MAPPING.containsKey(bs)) {
								bsid = TSCubePrimer.BLOCKSTATE_REVERSE_MAPPING.get(bs);
							} else {
								throw new CommandException("No mapping defined for " + bs);
							}
							bf.put(bsid);
						}
				DataOutputStream osWriter = new DataOutputStream(new FileOutputStream(file));
				osWriter.write(bf.array());
				osWriter.close();
			} else if (args[0].equalsIgnoreCase("clearship")) {
				TechnologicalSingularity.network.sendCommand((EntityPlayerMP)sender, ClientCommands.CLEAR_SHIP);
				sender.sendMessage(new TextComponentString("Clean."));
			} else if (args[0].equalsIgnoreCase("time")) {
				TechnologicalSingularity.network.sendCommand((EntityPlayerMP)sender, ClientCommands.SHOW_TIME);
				sender.sendMessage(new TextComponentString("Time: " + world.getTotalWorldTime()));
			} else if (args[0].equalsIgnoreCase("generateinbetweens")) {
				for(int ix=0;ix<2;ix++)
					for(int iy=0;iy<2;iy++)
						for(int iz=0;iz<2;iz++)	{
							file = getFile("cubes", args[1]+ix*2+iy*2+iz*2+".cube_structure");
							InputStream is = new FileInputStream(file);
							DataInputStream dis = new DataInputStream(is);
							byte[] data = new byte[4096];
							dis.read(data);
							dis.close();
							ByteBuffer bf = ByteBuffer.allocate(4096);
							if(ix==0){
								for (int index = 0; index < 4096; index++) {
									int dz = index >>> 8;
									int dy = (index >>> 4) & 15;
									int oldIndex = 15 << 8 | dy << 4 | dz;
									bf.put(data[oldIndex]);
								}
								String filename = args[1]+1+iy*2+iz*2+".cube_structure";
								this.writeCubeFile(filename, bf, sender);
							}
							if(iy==0){
								for (int index = 0; index < 4096; index++) {
									int dz = index >>> 8;
									int dx = index & 15;
									int oldIndex = dx << 8 | 15 << 4 | dz;
									bf.put(data[oldIndex]);
								}
								String filename = args[1]+ix*2+1+iz*2+".cube_structure";
								this.writeCubeFile(filename, bf, sender);
							}
							if(iz==0){
								for (int index = 0; index < 4096; index++) {
									int dy = (index >>> 4) & 15;
									int dx = index & 15;
									int oldIndex = dx << 8 | dy << 4 | 15;
									bf.put(data[oldIndex]);
								}
								String filename = args[1]+ix*2+iy*2+1+".cube_structure";
								this.writeCubeFile(filename, bf, sender);
							}
				}
				for(int ix=0;ix<=2;ix++)
					for(int iy=0;iy<=2;iy++)
						for(int iz=0;iz<=2;iz++)	{
							file = getFile("cubes", args[1]+ix+iy+iz+".cube_structure");
							if(!file.exists())
								continue;
							InputStream is = new FileInputStream(file);
							DataInputStream dis = new DataInputStream(is);
							byte[] data = new byte[4096];
							dis.read(data);
							dis.close();
							ByteBuffer bf = ByteBuffer.allocate(4096);
							if(ix==0 && (iy==1 || iz==1)) {
								for (int index = 0; index < 4096; index++) {
									int dz = index >>> 8;
									int dy = (index >>> 4) & 15;
									int oldIndex = 15 << 8 | dy << 4 | dz;
									bf.put(data[oldIndex]);
								}
								String filename = args[1]+1+iy+iz+".cube_structure";
								this.writeCubeFile(filename, bf, sender);
							}
							if(iy==0 && (ix==1 || iz==1)) {
								for (int index = 0; index < 4096; index++) {
									int dz = index >>> 8;
									int dx = index & 15;
									int oldIndex = dx << 8 | 15 << 4 | dz;
									bf.put(data[oldIndex]);
								}
								String filename = args[1]+ix+1+iz+".cube_structure";
								this.writeCubeFile(filename, bf, sender);
							}
							if(iz==0 && (ix==1 || iy==1)) {
								for (int index = 0; index < 4096; index++) {
									int dy = (index >>> 4) & 15;
									int dx = index & 15;
									int oldIndex = dx << 8 | dy << 4 | 15;
									bf.put(data[oldIndex]);
								}
								String filename = args[1]+ix+iy+1+".cube_structure";
								this.writeCubeFile(filename, bf, sender);
							}
				}
			} else if (args[0].equalsIgnoreCase("swapaxis")) {
				for (TSCubePrimer primer : TSCubePrimer.INSTANCES) {
					ByteBuffer bf = ByteBuffer.allocate(4096);
					for (int index = 0; index < 4096; index++) {
						int dz = index >>> 8;
						int dy = (index >>> 4) & 15;
						int dx = index & 15;
						int oldIndex = dx << 8 | dy << 4 | dz;
						bf.put(primer.data[oldIndex]);
					}
					this.writeCubeFile(primer.fileName, bf, sender);
				}
			} else if (args[0].equalsIgnoreCase("saveship")) {
				EntityPlayerMP player = (EntityPlayerMP)sender;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void writeCubeFile(String filename, ByteBuffer bf, ICommandSender sender){
		File file = getFile("cubes", filename);
		DataOutputStream osWriter;
		try {
			osWriter = new DataOutputStream(new FileOutputStream(file));
			osWriter.write(bf.array());
			osWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		sender.sendMessage(new TextComponentString("Done writing " + filename));
		bf.clear();
	}

	private void writeStructureToEBS(World world, BlockPos pos, int index, DataInputStream dis, boolean rotateCV,
			boolean mirrorX, boolean mirrorY, boolean mirrorZ) {
		try {
			while (dis.available() > 0) {
				int dx = index >>> 8;
				int dy = (index >>> 4) & 15;
				int dz = index & 15;
				int mappingIndex = dis.readUnsignedByte();
				IBlockState blockstate = TSCubePrimer.BLOCKSTATE_MAPPING[mappingIndex];
				if (blockstate == null) {
					throw new CommandException("No mapping defined for index=" + mappingIndex, mappingIndex);
				}
				if (mirrorX) {
					dx = 15 - dx;
					blockstate = blockstate.withMirror(Mirror.FRONT_BACK);
				}
				if (mirrorY) {
					dy = 15 - dy;
				}
				if (mirrorZ) {
					dz = 15 - dz;
					blockstate = blockstate.withMirror(Mirror.LEFT_RIGHT);
				}
				if (rotateCV) {
					dx = 15 - (index & 15);
					dz = index >>> 8;
					blockstate = blockstate.withRotation(Rotation.CLOCKWISE_90);
				}
				world.setBlockState(pos.east(dx).up(dy).south(dz), blockstate);
				index++;
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	public File getFile(String folder_name, String filename) {
		File folder = new File(TechnologicalSingularity.proxy.getMinecraftDir(), folder_name);
		folder.mkdirs();
		return new File(folder, filename);
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}
}
