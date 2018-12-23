package technological_singularity.block.state;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.Rotation;

public interface IRotatable {
	IBlockState withRotation(Axis a, Rotation rot);
}
