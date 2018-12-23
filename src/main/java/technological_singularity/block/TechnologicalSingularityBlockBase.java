package technological_singularity.block;

import static technological_singularity.TechnologicalSingularity.MODID;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import technological_singularity.TechnologicalSingularity;

public class TechnologicalSingularityBlockBase extends Block {

	public TechnologicalSingularityBlockBase(Material blockMaterialIn, MapColor blockMapColorIn) {
		super(blockMaterialIn, blockMapColorIn);
	}

	public TechnologicalSingularityBlockBase registerAs(String registryName, List<TechnologicalSingularityBlockBase> blocks){
		this.setRegistryName(new ResourceLocation(MODID,registryName));
		this.setUnlocalizedName(registryName);
		this.setCreativeTab(TechnologicalSingularity.tab);
		blocks.add(this);
		return this;
	}
	
	public Item generateItemFromBlock(){
		return new ItemBlock(this).setRegistryName(this.getRegistryName()).setCreativeTab(TechnologicalSingularity.tab);
	}

	public String stateToString(IBlockState state) {
		return "inventory";
	}
	
	@Override
    @SideOnly(Side.CLIENT)
	public int getPackedLightmapCoords(IBlockState state, IBlockAccess source, BlockPos pos) {
		return 15 << 20 | 15 << 4;
	}
}
