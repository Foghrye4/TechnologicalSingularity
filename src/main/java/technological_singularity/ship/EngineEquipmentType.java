package technological_singularity.ship;

import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import technological_singularity.tileentity.ShipEquipmentTileEntity;
import technological_singularity.util.TSStrings;

public class EngineEquipmentType extends EquipmentType {

	public final int thrustersAmount;
	public final float traction;

	public EngineEquipmentType(int idIn, String nameIn, int thrustersAmountIn, float tractionIn, List<EquipmentType> types) {
		super(idIn, nameIn, types);
		traction = tractionIn;
		thrustersAmount = thrustersAmountIn;
	}
}
