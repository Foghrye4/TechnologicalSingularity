package technological_singularity.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import static technological_singularity.TechnologicalSingularity.*;

public class BlockSpaceStationLightPanel extends TechnologicalSingularityBlockBase {

    protected static final AxisAlignedBB AABB_UP = new AxisAlignedBB(0.4375d, 0.0D, 0.4375d, 0.5625d, 0.125d, 0.5625d);
    protected static final AxisAlignedBB AABB_DOWN = new AxisAlignedBB(0.4375d, 0.875D, 0.4375d, 0.5625d, 1.0d, 0.5625d);
    protected static final AxisAlignedBB AABB_NORTH = new AxisAlignedBB(0.4375d, 0.4375d, 0.875D, 0.5625d, 0.5625d, 1.0d);
    protected static final AxisAlignedBB AABB_SOUTH = new AxisAlignedBB(0.4375d, 0.4375d, 0.0D, 0.5625d, 0.5625d, 0.125d);
    protected static final AxisAlignedBB AABB_WEST = new AxisAlignedBB(0.875d, 0.4375d, 0.4375d, 1.0d, 0.5625d, 0.5625d);
    protected static final AxisAlignedBB AABB_EAST = new AxisAlignedBB(0.0d, 0.4375d, 0.4375d, 0.125d, 0.5625d, 0.5625d);

    
    public static final PropertyDirection FACING = PropertyDirection.create("facing");
    
	public BlockSpaceStationLightPanel(Material blockMaterialIn, MapColor blockMapColorIn) {
		super(blockMaterialIn, blockMapColorIn);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP));
	}
	
	@Override
	public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return MapColor.QUARTZ;
	}
	
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
    	switch(state.getValue(FACING)){
    	case UP:
    		return AABB_UP;
    	case DOWN:
    		return AABB_DOWN;
    	case SOUTH:
    		return AABB_SOUTH;
    	case NORTH:
    		return AABB_NORTH;
    	case WEST:
    		return AABB_WEST;
    	case EAST:
    		return AABB_EAST;
    	}
		return AABB_UP;
    }
	
	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
		for (EnumFacing facing : EnumFacing.values())
			items.add(new ItemStack(this, 1, facing.getIndex()));
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return ((EnumFacing) state.getValue(FACING)).getIndex();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {FACING});
	}
	
	@Override
	public Item generateItemFromBlock(){
		return new ItemMultiTexture(this, null, new ItemMultiTexture.Mapper(){

			@Override
			public String apply(ItemStack stack) {
				return EnumFacing.getFront(stack.getMetadata()).getName();
			}}).setRegistryName(this.getRegistryName()).setCreativeTab(tab);
	}
	
	@Override
	public String stateToString(IBlockState state) {
		return FACING.getName()+"="+state.getProperties().get(FACING).toString();
	}
	
	@Override 
	public boolean isFullBlock(IBlockState state) {
		return false;
	}
	
	@Override 
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
}
