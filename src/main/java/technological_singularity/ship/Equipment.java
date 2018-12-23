package technological_singularity.ship;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Map.Entry;

import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.GameData;
import technological_singularity.util.TSConstants;

public class Equipment {
	public final EquipmentType equipmentType;
	public final BlockPos localShipPos;
	private final Object2FloatMap<String> properties;
	private final Object2FloatMap<String> globalModificators;
	public final IBlockState state;
	public final int[] controls;

	public Equipment(EquipmentType equipmentTypeIn, IBlockState stateIn, BlockPos localShipPosIn,
			Object2FloatMap<String> propertiesIn, Object2FloatMap<String> globalModificatorsIn, int[] controlsIn) {
		localShipPos = localShipPosIn;
		state = stateIn;
		equipmentType = equipmentTypeIn;
		properties = propertiesIn;
		globalModificators = globalModificatorsIn;
		controls = controlsIn;
	}

	public float getValueOrDefault(String name, float defaultValue) {
		return properties.getOrDefault(name, defaultValue);
	}

	public void writeToByteBuffer(ByteBuffer buffer) {
		buffer.putInt(equipmentType.id);
		buffer.putInt(localShipPos.getX());
		buffer.putInt(localShipPos.getY());
		buffer.putInt(localShipPos.getZ());
		int blockStateId = GameData.getBlockStateIDMap().get(state);
		buffer.putInt(blockStateId);
		for (int i = 0; i < controls.length; i++)
			buffer.putInt(controls[i]);
		buffer.putInt(properties.size());
		for (Entry<String, Float> entry : properties.entrySet()) {
			char[] keyCharArray = entry.getKey().toCharArray();
			buffer.putInt(keyCharArray.length);
			for (char charIn : keyCharArray)
				buffer.putChar(charIn);
			buffer.putFloat(entry.getValue());
		}
		buffer.putInt(globalModificators.size());
		for (Entry<String, Float> entry : globalModificators.entrySet()) {
			char[] keyCharArray = entry.getKey().toCharArray();
			buffer.putInt(keyCharArray.length);
			for (int i = 0; i < keyCharArray.length; i++)
				buffer.putChar(keyCharArray[i]);
			buffer.putFloat(entry.getValue());
		}
	}

	public static Equipment readFromByteBuffer(ByteBuffer buffer) {
		EquipmentType type = EquipmentRegistry.get(buffer.getInt());
		int x = buffer.getInt();
		int y = buffer.getInt();
		int z = buffer.getInt();
		BlockPos localShipPos = new BlockPos(x, y, z);
		int blockStateId = buffer.getInt();
		IBlockState state = GameData.getBlockStateIDMap().getByValue(blockStateId);
		int[] controlsIn = new int[TSConstants.MAX_THRUSTERS_PER_UNIT];
		for (int i = 0; i < TSConstants.MAX_THRUSTERS_PER_UNIT; i++) {
			controlsIn[i] = buffer.getInt();
		}
		Object2FloatMap<String> properties = new Object2FloatOpenHashMap<String>();
		int propertyMapSize = buffer.getInt();
		for (int i = 0; i < propertyMapSize; i++) {
			int keyLength = buffer.getInt();
			char[] keyData = new char[keyLength];
			for (int i1 = 0; i < keyLength; i++)
				keyData[i1] = buffer.getChar();
			String key = new String(keyData);
			properties.put(key, buffer.getFloat());
		}
		Object2FloatMap<String> globalModificators = new Object2FloatOpenHashMap<String>();
		int globalModificatorsMapSize = buffer.getInt();
		for (int i = 0; i < globalModificatorsMapSize; i++) {
			int keyLength = buffer.getInt();
			char[] keyData = new char[keyLength];
			for (int i1 = 0; i < keyLength; i++)
				keyData[i1] = buffer.getChar();
			String key = new String(keyData);
			globalModificators.put(key, buffer.getFloat());
		}
		if(state==null)
			throw new NullPointerException("Game data return null block state while decoding ship equipment. Block state id=" + blockStateId);
		return new Equipment(type, state, localShipPos, properties, globalModificators, controlsIn);
	}
}
