package technological_singularity.item;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import technological_singularity.block.state.IRotatable;

public class ItemRotator extends TechnologicalSingularityItemBase {

	private final Axis a;

	public ItemRotator(Axis aIn) {
		super();
		a = aIn;
	}

	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote) {
			IBlockState bstate = worldIn.getBlockState(pos);
			if (bstate instanceof IRotatable) {
				IRotatable rbState = (IRotatable) bstate;
				bstate = rbState.withRotation(a, Rotation.CLOCKWISE_90);
			} else {
				bstate = bstate.withRotation(Rotation.CLOCKWISE_90);
			}
			worldIn.setBlockState(pos, bstate);
		}
		return EnumActionResult.SUCCESS;
	}
}
