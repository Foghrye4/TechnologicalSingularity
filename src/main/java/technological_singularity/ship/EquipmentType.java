package technological_singularity.ship;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import technological_singularity.tileentity.ShipEquipmentTileEntity;

public class EquipmentType implements Comparable<EquipmentType> {
	int price = 1;
	float mass = 1000.0f; // kg
	int durability = 1;
	final int id;
	final String name;

	public EquipmentType(int idIn, String nameIn, List<EquipmentType> types) {
		id = idIn;
		name = nameIn;
		types.add(this);
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int compareTo(EquipmentType o) {
		return this.id - o.id;
	}

	public Equipment generateEquipment(World world, IBlockState state, BlockPos pos, BlockPos downNorthWestCorner) {
		ShipEquipmentTileEntity te = (ShipEquipmentTileEntity) world.getTileEntity(pos);
		return new Equipment(this, state, pos.subtract(downNorthWestCorner), te.getProperties(),te.getGlobalModificators(), te.getThrustersControls());
	}
}
