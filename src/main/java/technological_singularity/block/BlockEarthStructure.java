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
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import static technological_singularity.TechnologicalSingularity.*;

public class BlockEarthStructure extends TechnologicalSingularityBlockBase {

	public static final PropertyEnum<BlockEarthStructure.EnumType> VARIANT = PropertyEnum.<BlockEarthStructure.EnumType>create(
			"variant", BlockEarthStructure.EnumType.class);

	public BlockEarthStructure(Material blockMaterialIn, MapColor blockMapColorIn) {
		super(blockMaterialIn, blockMapColorIn);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockEarthStructure.EnumType.REGULAR));
	}
	
	@Override
	public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return ((BlockEarthStructure.EnumType) state.getValue(VARIANT)).getMapColor();
	}
	
	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
		for (EnumType type : EnumType.values())
			items.add(new ItemStack(this, 1, type.getMetadata()));
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(VARIANT, BlockEarthStructure.EnumType.byMetadata(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return ((BlockEarthStructure.EnumType) state.getValue(VARIANT)).getMetadata();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {VARIANT});
	}
	
	@Override
	public Item generateItemFromBlock(){
		return new ItemMultiTexture(this, null, new ItemMultiTexture.Mapper(){

			@Override
			public String apply(ItemStack stack) {
				return EnumType.byMetadata(stack.getMetadata()).getName();
			}}).setRegistryName(this.getRegistryName()).setCreativeTab(tab);
	}
	
	@Override
	public String stateToString(IBlockState state) {
		return VARIANT.getName()+"="+state.getProperties().get(VARIANT).toString();
	}
	
	public static enum EnumType implements IStringSerializable {
		REGULAR(0, MapColor.LIGHT_BLUE, "regular"),
		BLACK_TILES(1, MapColor.BLACK_STAINED_HARDENED_CLAY, "black_tiles"),
		ASPHALT(2, MapColor.BLACK_STAINED_HARDENED_CLAY, "asphalt"),
		ASPHALT_STRIPED(3, MapColor.BLACK_STAINED_HARDENED_CLAY, "asphalt_striped"),
		HYDROCONCRETE(4, MapColor.BLACK_STAINED_HARDENED_CLAY, "hydroconcrete");

		/** Array of the Block's BlockStates */
		private static final BlockEarthStructure.EnumType[] META_LOOKUP = new BlockEarthStructure.EnumType[values().length];
		/** The BlockState's metadata. */
		private final int meta;
		/** The EnumType's name. */
		private final String name;
		private final MapColor mapColor;

		private EnumType(int metaIn, MapColor colorIn, String nameIn) {
			this.meta = metaIn;
			this.name = nameIn;
			this.mapColor = colorIn;
		}

		public static EnumType byMetadata(int meta) {
			if (meta < 0 || meta >= META_LOOKUP.length) {
				meta = 0;
			}
			return META_LOOKUP[meta];
		}

		/**
		 * Returns the EnumType's metadata value.
		 */
		public int getMetadata() {
			return this.meta;
		}

		public MapColor getMapColor() {
			return this.mapColor;
		}

		@Override
		public String getName() {
			return this.name;
		}

		@Override
		public String toString() {
			return this.name;
		}

		public String getUnlocalizedName() {
			return this.name;
		}
		
		static {
			for (BlockEarthStructure.EnumType blockstone$enumtype : values()) {
				META_LOOKUP[blockstone$enumtype.getMetadata()] = blockstone$enumtype;
			}
		}

	}
}
