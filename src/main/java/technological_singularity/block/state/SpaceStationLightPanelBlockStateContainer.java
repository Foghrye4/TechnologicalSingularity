package technological_singularity.block.state;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import technological_singularity.block.properties.PropertySlopeNormal;

public class SpaceStationLightPanelBlockStateContainer extends BlockStateContainer {
	private IBlockState[] propertyValueArray = new IBlockState[6];
	
	 public SpaceStationLightPanelBlockStateContainer(Block blockIn, PropertySlopeNormal variant) {
	        super(blockIn, variant);
	    }

	    protected StateImplementation createState(Block block, ImmutableMap<IProperty<?>, Comparable<?>> properties,
	            @Nullable ImmutableMap<net.minecraftforge.common.property.IUnlistedProperty<?>, java.util.Optional<?>> unlistedProperties) {
	    	if (propertyValueArray == null)
	            propertyValueArray = new IBlockState[6];
	        return new SpaceStationLightPanelStateImplementation(block, properties, propertyValueArray);
	    }
}
