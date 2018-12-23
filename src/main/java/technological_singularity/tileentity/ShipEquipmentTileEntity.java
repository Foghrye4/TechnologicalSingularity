package technological_singularity.tileentity;

import java.util.Map.Entry;

import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import technological_singularity.block.ShipEquipmentBlock;
import technological_singularity.ship.EngineEquipmentType;
import technological_singularity.util.TSConstants;
import technological_singularity.util.TSStrings;

public class ShipEquipmentTileEntity extends TileEntity {
	
	private final Object2FloatMap<String> properties = new Object2FloatOpenHashMap<String>();
	private final Object2FloatMap<String> globalModificators = new Object2FloatOpenHashMap<String>();
	private final int[] controls = new int[TSConstants.MAX_THRUSTERS_PER_UNIT];

	public int[] getThrustersControls() {
		return controls;
	}

	public Object2FloatMap<String> getProperties() {
		return properties;
	}

	public Object2FloatMap<String> getGlobalModificators() {
		return globalModificators;
	}

	public int getThrustersAmount() {
		return ((EngineEquipmentType)((ShipEquipmentBlock)world.getBlockState(pos).getBlock()).equipmentType).thrustersAmount;
	}

	public void switchThrusterControl(int thruster, int control) {
		int mask = 1<<control;
		controls[thruster]^=mask;
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound = super.writeToNBT(compound);
		NBTTagCompound propertiesNBT = new NBTTagCompound();
		NBTTagCompound globalModificatorsNBT = new NBTTagCompound();
		for(Entry<String, Float> entry:properties.entrySet()){
			propertiesNBT.setFloat(entry.getKey(), entry.getValue());
		}
		for(Entry<String, Float> entry:globalModificators.entrySet()){
			globalModificatorsNBT.setFloat(entry.getKey(), entry.getValue());
		}
		compound.setTag(TSStrings.NBT_PROPERTIES, propertiesNBT);
		compound.setTag(TSStrings.NBT_GLOBAL_MODIFICATORS, globalModificatorsNBT);
		compound.setIntArray(TSStrings.NBT_CONTROLS, controls);
		return compound;
	}
	
    public void readFromNBT(NBTTagCompound compound)
    {
    	super.readFromNBT(compound);
		NBTTagCompound propertiesNBT = compound.getCompoundTag(TSStrings.NBT_PROPERTIES);
		NBTTagCompound globalModificatorsNBT = compound.getCompoundTag(TSStrings.NBT_GLOBAL_MODIFICATORS);
		for(String key:propertiesNBT.getKeySet()){
			properties.put(key, propertiesNBT.getFloat(key));
		}
		for(String key:globalModificatorsNBT.getKeySet()){
			globalModificators.put(key, globalModificatorsNBT.getFloat(key));
		}
		int[] controlsIn = compound.getIntArray(TSStrings.NBT_CONTROLS);
		for(int i=0;i<controls.length;i++){
			controls[i] = controlsIn[i];
		}
    }

	
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound nbt = super.getUpdateTag();
		nbt.setIntArray("controls", controls);
		return nbt;
	}
}
