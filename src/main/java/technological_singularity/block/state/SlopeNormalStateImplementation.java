package technological_singularity.block.state;

import com.google.common.collect.ImmutableMap;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer.StateImplementation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.Vec3i;
import technological_singularity.block.properties.PropertySlopeNormal;
import technological_singularity.util.TSMathHelper;

public class SlopeNormalStateImplementation extends StateImplementation implements IRotatable{

	private final IBlockState[] propertyValueArray;
	private final Vec3i value;
	private final PropertySlopeNormal property;

	protected SlopeNormalStateImplementation(Block blockIn, ImmutableMap<IProperty<?>, Comparable<?>> propertiesIn,
			IBlockState[] propertyValueArrayIn) {
		super(blockIn, propertiesIn);
		property = (PropertySlopeNormal) propertiesIn.keySet().iterator().next();
		value = (Vec3i) propertiesIn.get(property);
		propertyValueArray = propertyValueArrayIn;
		propertyValueArray[property.toMeta(value)] = this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Comparable<T>> T getValue(IProperty<T> property) {
		return (T) value;
	}

	@Override
	public <T extends Comparable<T>, V extends T> IBlockState withProperty(IProperty<T> property, V value) {
		return propertyValueArray[((PropertySlopeNormal) property).toMeta((Vec3i) value)];
	}

	@Override
	public IBlockState withRotation(Rotation rot) {
		return this.withProperty(property, TSMathHelper.rotateVec3i(Axis.Y, rot, value));
	}

	@Override
	public IRotatable withRotation(Axis a, Rotation rot) {
		return (IRotatable) this.withProperty(property, TSMathHelper.rotateVec3i(a, rot, value));
	}
}
