package technological_singularity.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import technological_singularity.block.properties.PropertySlopeNormal;
import technological_singularity.block.state.SlopeBlockStateContainer;
import technological_singularity.ship.EquipmentType;
import technological_singularity.util.TSMathHelper;

import static technological_singularity.TechnologicalSingularity.*;

public class BlockArmorEquipmentSlope extends ShipEquipmentBlock {

	public static final PropertySlopeNormal VARIANT = new PropertySlopeNormal();

	public BlockArmorEquipmentSlope(EquipmentType equipmentTypeIn, Material blockMaterialIn, MapColor blockMapColorIn) {
		super(equipmentTypeIn, blockMaterialIn, blockMapColorIn);
		this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, new Vec3i(1, 1, 0)));
	}

	@Override
	public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return MapColor.BLUE;
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

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer) {
		for (Vec3i vec : VARIANT.getAllowedValues()) {
			for (BlockPos mPos : BlockPos.getAllInBoxMutable(pos.subtract(vec), pos)) {
				if (mPos.distanceSq(pos) == 1) {
					if (worldIn.getBlockState(mPos).getBlock() instanceof ShipEquipmentBlock) {
						return this.getBlockState().getBaseState().withProperty(VARIANT, vec);
					}
				}
			}
		}
		return this.getDefaultState();
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		Vec3i vec = state.getValue(VARIANT);
		for (BlockPos mPos : BlockPos.getAllInBoxMutable(pos.subtract(vec), pos)) {
			if (mPos.distanceSq(pos) == 1) {
				if (worldIn.getBlockState(mPos).getBlock() instanceof ShipEquipmentBlock) {
					return;
				}
			}
		}
		this.dropBlockAsItem(worldIn, pos, state, 0);
		worldIn.setBlockToAir(pos);
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		for (Vec3i vec : VARIANT.getAllowedValues()) {
			for (BlockPos mPos : BlockPos.getAllInBoxMutable(pos.subtract(vec), pos)) {
				if (mPos.distanceSq(pos) == 1) {
					if (worldIn.getBlockState(mPos).getBlock() instanceof ShipEquipmentBlock) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(VARIANT, TSMathHelper.rotateVec3i(Axis.Y, rot, state.getValue(VARIANT)));
	}

	@Override
	public String stateToString(IBlockState state) {
		return VARIANT.getName() + "=" + VARIANT.getName((Vec3i) state.getProperties().get(VARIANT));
	}

	@Override
	public boolean isFullBlock(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public Vec3d[] getDirections(IBlockState state, BlockPos pos1) {
		Vec3i slopeNormal = state.getValue(VARIANT);
		Vec3d primary;
		Vec3d secondary;
		if (slopeNormal.getX() == 0) {
			primary = new Vec3d(0, 0, slopeNormal.getZ());
			secondary = new Vec3d(0, slopeNormal.getY(), 0);
		} else if (slopeNormal.getY() == 0) {
			primary = new Vec3d(slopeNormal.getX(), 0, 0);
			secondary = new Vec3d(0, 0, slopeNormal.getZ());
		} else {
			primary = new Vec3d(slopeNormal.getX(), 0, 0);
			secondary = new Vec3d(0, slopeNormal.getY(), 0);
		}
		return new Vec3d[] { primary, secondary };
	}
	
	@Override
	public Vec3d[] getPositions(IBlockState state, BlockPos pos) {
		return new Vec3d[] { 
				new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5),
				new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) 
				};
	}

}
