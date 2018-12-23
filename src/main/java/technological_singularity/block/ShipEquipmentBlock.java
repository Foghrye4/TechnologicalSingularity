package technological_singularity.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import technological_singularity.TechnologicalSingularity;
import technological_singularity.init.TSEquipmentTypes;
import technological_singularity.ship.EngineEquipmentType;
import technological_singularity.ship.EquipmentType;
import technological_singularity.tileentity.ShipEquipmentTileEntity;

public class ShipEquipmentBlock extends TechnologicalSingularityBlockBase implements ITileEntityProvider {

	public final EquipmentType equipmentType;

	public ShipEquipmentBlock(EquipmentType equipmentTypeIn, Material blockMaterialIn, MapColor blockMapColorIn) {
		super(blockMaterialIn, blockMapColorIn);
		equipmentType = equipmentTypeIn;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new ShipEquipmentTileEntity();
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		if (equipmentType == TSEquipmentTypes.CORE)
			return;
		for (BlockPos mPos : BlockPos.getAllInBoxMutable(pos.add(-1, -1, -1), pos.add(1, 1, 1))) {
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
		if (equipmentType == TSEquipmentTypes.CORE)
			return true;
		for (BlockPos mPos : BlockPos.getAllInBoxMutable(pos.add(-1, -1, -1), pos.add(1, 1, 1))) {
			if (mPos.distanceSq(pos) == 1) {
				if (worldIn.getBlockState(mPos).getBlock() instanceof ShipEquipmentBlock) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		if(!player.getHeldItem(EnumHand.MAIN_HAND).isEmpty())
			return false;
		if (equipmentType instanceof EngineEquipmentType)
			player.openGui(TechnologicalSingularity.instance, 1, world, pos.getX(), pos.getY(), pos.getZ());
		else
			player.openGui(TechnologicalSingularity.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	public Vec3d[] getDirections(IBlockState state, BlockPos pos) {
		return new Vec3d[0];
	}

	public Vec3d[] getPositions(IBlockState state, BlockPos pos) {
		return new Vec3d[] { new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) };
	}
}
