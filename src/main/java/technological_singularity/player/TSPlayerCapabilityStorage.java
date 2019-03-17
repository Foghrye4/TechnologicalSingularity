package technological_singularity.player;

import java.nio.ByteBuffer;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import technological_singularity.ship.Ship;

public class TSPlayerCapabilityStorage implements IStorage<ITSPlayer> {

	@Override
	public NBTBase writeNBT(Capability<ITSPlayer> capability, ITSPlayer instance, EnumFacing side) {
		Ship ship = instance.getShip();
		if (ship == null)
			return null;

		ByteBuffer bb = ByteBuffer.allocate(Ship.MAX_SERIALIZED_SIZE);
		ship.writeToByteBuffer(bb);
		NBTTagByteArray nbt = new NBTTagByteArray(bb.array());
		return nbt;
	}

	@Override
	public void readNBT(Capability<ITSPlayer> capability, ITSPlayer instance, EnumFacing side, NBTBase nbt) {
		if (nbt == null) {
			instance.setShip(null);
			return;
		}
		NBTTagByteArray nbtc = (NBTTagByteArray) nbt;
		Ship ship = Ship.readFromByteBuffer(ByteBuffer.wrap(nbtc.getByteArray()));
		instance.setShip(ship);
	}

}
