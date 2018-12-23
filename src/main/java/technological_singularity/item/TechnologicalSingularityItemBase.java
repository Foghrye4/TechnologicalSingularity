package technological_singularity.item;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import technological_singularity.TechnologicalSingularity;

import static technological_singularity.TechnologicalSingularity.*;

import java.util.List;

public class TechnologicalSingularityItemBase extends Item {
	public TechnologicalSingularityItemBase registerAs(String registryName, List<TechnologicalSingularityItemBase> items){
		this.setRegistryName(new ResourceLocation(MODID,registryName));
		this.setUnlocalizedName(registryName);
		this.setCreativeTab(TechnologicalSingularity.tab);
		items.add(this);
		return this;
	}

	public String getModelLocation(int meta) {
		return "inventory";
	}

	public int getLastMeta() {
		return 0;
	}
}
