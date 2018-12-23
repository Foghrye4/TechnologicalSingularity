package technological_singularity.block.state;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer.StateImplementation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import technological_singularity.block.BlockSpaceStationLightPanel;
import technological_singularity.block.IBlockLightSource;
import technological_singularity.block.properties.PropertySlopeNormal;

public class SpaceStationLightPanelStateImplementation extends StateImplementation implements IBlockLightSource {

	private final IBlockState[] propertyValueArray;
	private final EnumFacing value;

	protected SpaceStationLightPanelStateImplementation(Block blockIn, ImmutableMap<IProperty<?>, Comparable<?>> propertiesIn,
			IBlockState[] propertyValueArrayIn) {
		super(blockIn, propertiesIn);
		PropertyDirection property = (PropertyDirection) propertiesIn.keySet().iterator().next();
		value = (EnumFacing) propertiesIn.get(property);
		propertyValueArray = propertyValueArrayIn;
		propertyValueArray[value.getIndex()] = this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Comparable<T>> T getValue(IProperty<T> property) {
		return (T) value;
	}

	@Override
	public <T extends Comparable<T>, V extends T> IBlockState withProperty(IProperty<T> property, V value) {
		return propertyValueArray[((EnumFacing) value).getIndex()];
	}
	
	@Override
	public Vec3i getLightSourceDirection() {
		return value.getDirectionVec();
	}

	@Override
	public Vec3d getPos(int x, int y, int z) {
		AxisAlignedBB bb = this.getBlock().getBoundingBox(this, null, null);
		return new Vec3d(bb.minX + (bb.maxX - bb.minX) * 0.5D + x, bb.minY + (bb.maxY - bb.minY) * 0.5D + y, bb.minZ + (bb.maxZ - bb.minZ) * 0.5D + z);
	}
	
	// To detect if this light is a spotlight without checking class interfaces
	@Override
	public int getLightValue() {
		return 255;
	}
}
