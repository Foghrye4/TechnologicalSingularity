package technological_singularity.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import technological_singularity.block.properties.PropertySlopeNormal;
import technological_singularity.block.state.SlopeBlockStateContainer;
import technological_singularity.util.TSMathHelper;

import static technological_singularity.TechnologicalSingularity.*;

public class BlockEarthStructureSlope extends TechnologicalSingularityBlockBase {

	public static final PropertySlopeNormal VARIANT = new PropertySlopeNormal();

	public BlockEarthStructureSlope(Material blockMaterialIn, MapColor blockMapColorIn) {
		super(blockMaterialIn, blockMapColorIn);
		this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, new Vec3i(1, 1, 0)));
	}

	@Override
	public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return MapColor.LIGHT_BLUE;
	}

	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
		for (int i = 0; i < 12; i++)
			items.add(new ItemStack(this, 1, i));
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(VARIANT, VARIANT.fromMeta(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return VARIANT.toMeta(state.getValue(VARIANT));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new SlopeBlockStateContainer(this, VARIANT);
	}

	@Override
	public Item generateItemFromBlock() {
		return new ItemMultiTexture(this, null, new ItemMultiTexture.Mapper() {

			@Override
			public String apply(ItemStack stack) {
				return VARIANT.getName(VARIANT.fromMeta(stack.getMetadata()));
			}
		}).setRegistryName(this.getRegistryName()).setCreativeTab(tab);
	}

	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(VARIANT, TSMathHelper.rotateVec3i(Axis.Y, rot, state.getValue(VARIANT)));
	}

	@Override
	public String stateToString(IBlockState state) {
		return VARIANT.getName()+"="+VARIANT.getName((Vec3i) state.getProperties().get(VARIANT));
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
