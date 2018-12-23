package technological_singularity.creativetab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import technological_singularity.init.TSBlocks;

public class TSCreativeTab extends CreativeTabs {

	public TSCreativeTab(String label) {
		super(label);
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(TSBlocks.EARTH_STRUCTURE);
	}
}
