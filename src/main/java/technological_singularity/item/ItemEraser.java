package technological_singularity.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemEraser extends TechnologicalSingularityItemBase {

	BlockPos from;
	BlockPos to;

	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote) {
			if (from == null) {
				from = pos;
			} else if (to == null) {
				to = pos;
			} else {
				if (to.equals(pos)) {
					BlockPos.getAllInBox(from, to).forEach(b -> {
						worldIn.setBlockToAir(b);
					});
				}
				from = null;
				to = null;
			}
		}
		return EnumActionResult.SUCCESS;
	}
}