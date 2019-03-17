package technological_singularity.block.state;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.Rotation;

public interface IRotatable extends IBlockState {
	IRotatable withRotation(Axis a, Rotation rot);
}
